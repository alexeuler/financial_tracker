package com.financetracker.state

import fs2.{Strategy, Stream, Task}
import org.scalatest.{AsyncFunSpec, BeforeAndAfter, Matchers}
import StateFold._
import cats.data.WriterT


class StateClientSpec extends AsyncFunSpec with Matchers with BeforeAndAfter {
  implicit val strategy = Strategy.fromFixedDaemonPool(4)

  it("Provides thread-safe way of updating the app state") {
    val cli = new StateClientImpl(State("John", Cash(0)), fold)

    // Initiate 10k tasks on the thread pool of 4 threads
    val res = for {
      _ <- Task.parallelTraverse(1 to 10000)(_ => cli.dispatch(IncreaseCashBy(1)))
    } yield cli.get.cash.amount shouldBe 10000

    res.unsafeRunAsyncFuture()
  }

  it("processes event streams from fold function") {
    val cli = new StateClientImpl(State("", Cash(0)), fold)

    val res = for {
      _ <- cli.dispatch(LogAsync(List("1", "2", "3")))
    } yield cli.get.log shouldBe "1, 2, 3, "

    res.unsafeRunAsyncFuture()
  }

  it("processes events streams from several fold functions asynchronously") {
    val cli = new StateClientImpl(State("", Cash(0)), fold)

    val res = for {
      _ <- Task.parallelTraverse(1 to 1000)(_ => cli.dispatch(LogAsync(List("1", "2", "3"))))
    } yield cli.get.log should not be (1 to 1000).flatMap(_ => List(1, 2, 3)).mkString(", ") + ", "

    res.unsafeRunAsyncFuture()
  }

  case class Cash(amount: Int)
  case class State(log: String, cash: Cash)

  trait Event
  case class IncreaseCashBy(amount: Int) extends Event
  case class Log(s: String) extends Event
  case class LogAsync(list: List[String]) extends Event
  case object InconsistentState extends Event

  val fold: StateFold[State, Event] = StateFold[State, Event](
    (state, event) => event match {
      case IncreaseCashBy(amount) => (
        state.copy(cash = state.cash.copy(amount = state.cash.amount + amount)),
        StateFold.empty
      )
      case Log(s) => (
        state.copy(log = s"${state.log}$s, "),
        StateFold.empty
      )
      case LogAsync(list: List[String]) =>
        (
          state,
          traverse(list)(s => tell[Event](Log(s)))
        )
      case _ => (state, StateFold.empty)
    }
  ) 
}

