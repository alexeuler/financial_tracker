package com.financetracker.state

import app.helpers.AsyncPropertyChecks
import fs2.{Strategy, Task}
import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.prop.PropertyChecks
import ConnectionState._
import cats.data.EitherT

class ConnectionStateSpec extends FunSpec with AsyncPropertyChecks with Matchers with BeforeAndAfter with PropertyChecks {
  var closeCounter = 0
  val closeConn = (_: Unit) => EitherT[Task, Throwable, Unit](Task.delay({ closeCounter += 1; Right(()) }))


  describe("100% stable system") {
    var createCounter = 0
    val stableCreateConn = EitherT[Task, Throwable, Unit](Task.delay({ createCounter += 1; Right(()) }))
    val maxConsequentFailures = 10
    val fold = ConnectionState.fold("SomeConnection")(maxConsequentFailures)(stableCreateConn)(closeConn)

    it("creates connection one time, never closes it and ends up in a cached state") {
      forAllAsync(successfulEvents) { events =>
        createCounter = 0
        closeCounter = 0
        val stateClient = StateClientImpl(Empty, fold)
        for {
          _ <- Task.traverse(events)(event => stateClient.dispatch(event))
        } yield {
          stateClient.get should matchPattern { case Cached(_, 0) => }
          createCounter shouldBe 1
          closeCounter shouldBe 0
        }
      }
    }
  }

  describe("random events, failing create connection") {
    val failingCreateConn = EitherT[Task, Throwable, Unit](Task.delay(throw new Throwable).attempt)
    val maxConsequentFailures = 2
    val fold = ConnectionState.fold("SomeConnection")(maxConsequentFailures)(failingCreateConn)(closeConn)

    it("never reaches cached state and never closes connection") {
      forAllAsync(randomEvents) { events =>
        closeCounter = 0
        val stateClient = StateClientImpl(Empty, fold)
        for {
          _ <- Task.traverse(events)(event => stateClient.dispatch(event))
        } yield {
          stateClient.get should not matchPattern { case Cached(_, 0) => }
          closeCounter shouldBe 0
        }
      }
    }
  }

  describe("random events, stable create connection") {
    var createCounter = 0
    val stableCreateConn = EitherT[Task, Throwable, Unit](Task.delay({ createCounter += 1; Right(()) }))
    val maxConsequentFailures = 2
    val fold = ConnectionState.fold("SomeConnection")(maxConsequentFailures)(stableCreateConn)(closeConn)

    it("tries to recreate connection several times, and match create and close counts") {
      forAllAsync(randomEvents) { events =>
        createCounter = 0
        closeCounter = 0
        val stateClient = StateClientImpl(Empty, fold)
        for {
          _ <- Task.traverse(events)(event => stateClient.dispatch(event))
        } yield {
          // 3 in this case is a magical number based on several runs.
          // Ideally we would have to calculate probability here.
          createCounter should be > 3
          createCounter - closeCounter should be <= 1
          createCounter - closeCounter should be >= 0
        }
      }
    }
  }


  private val threadCount = 4
  private implicit val strategy = Strategy.fromFixedDaemonPool(threadCount)

  private val minEventListLength = 100
  private val maxEventListLength = 200

  val randomEvent: Gen[Event] =
    Gen.oneOf[Event](
      ConnectionState.CreateRequest,
      ConnectionState.IncreaseFailureCount,
      ConnectionState.ResetFailureCount
    )

  val randomEvents: Gen[List[Event]] =
    for {
      n <- Gen.choose(minEventListLength, maxEventListLength)
      list <- Gen.listOfN(n, randomEvent)
    } yield list

  val successfulEvent: Gen[Event] =
    Gen.oneOf[Event](
      ConnectionState.CreateRequest,
      ConnectionState.ResetFailureCount
    )

  val successfulEvents: Gen[List[Event]] =
    for {
      n <- Gen.choose(minEventListLength, maxEventListLength)
      list <- Gen.listOfN(n, successfulEvent)
    } yield list
}