package com.financetracker.state

import StateFold._
import fs2.util.Attempt
import fs2.interop.cats._
import cats.instances.list._
import com.financetracker.types._

/**
  *
  *                  IncreaseFailureCount
  *                (if surpassed threshold)
  *    ←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←      →→
  *   ↓                                                             ↑    ↑  ↑
  *   ↓                                                             ↑    ↑  ↑
  * Empty →→→→→→→→→→→→→→→→→→→→→→→ Retrieving →→→→→→→→→→→→→→→→→→→→→→ Cached  ↑
  *   ↑       CreateRequest       ↓                CreateSuccess         ↓  ↑
  *   ↑                           ↓                                      ↓  ↑
  *   ↑                           ↓                                       ←←
  *    ←←←←←←←←←←←←←←←←←←←←←←←←←←←                                   IncreaseFailureCount (if didn't surpass threshold)
  *           CreateFailure                                          or ResetFailureCount
  */

object ConnectionState {

  trait State
  case object Empty extends State
  case object Retrieving extends State
  case class Cached[A](conn: A, consequentFailures: Int) extends State

  trait Event
  case object CreateRequest extends Event
  case object IncreaseFailureCount extends Event
  case object ResetFailureCount extends Event
  case class LogInfo(message: String) extends Event
  case class LogError(message: String) extends Event

  private case class CreateSuccess[A](conn: A) extends Event
  private case class CreateFailure(e: Throwable) extends Event


  def fold[A]: String => Int => TaskAttempt[A] => (A => TaskAttempt[Unit]) => StateFold[State, Event] =
    name => maxFailureCount => createConn => closeConn => StateFold {
      case (Empty, CreateRequest) => (
        Retrieving,
        for {
          _ <- tell[Event](LogInfo(s"$name: Creating connection"))
          result <- pureTask[Event, Attempt[A]](createConn.value)
          _ <- result.fold(e => tell[Event](CreateFailure(e)), (conn: A) => tell[Event](CreateSuccess(conn)))
        } yield ()
      )

      case (Retrieving, CreateFailure(e)) => (
        Empty,
        tell(LogError(s"$name: Error creating connection: ${e.getMessage}"))
      )

      case (Retrieving, CreateSuccess(cluster)) => (
        Cached(cluster, 0),
        tell[Event](LogInfo(s"$name: Successfully created connection"))
      )

      case (Cached(cluster, _), ResetFailureCount) => (
        Cached(cluster, 0),
        empty
      )

      case (Cached(cluster, count), IncreaseFailureCount) if count + 1 < maxFailureCount => (
        Cached(cluster, count + 1),
        empty
      )

      case (Cached(conn, _), IncreaseFailureCount) => (
        Empty,
        for {
          closeResult <- pureTask[Event, Attempt[Unit]](closeConn(conn.asInstanceOf[A]).value)
          _ <- closeResult.fold(
            e => tell[Event](LogError(s"$name: Failed to close connection: ${e.getMessage}")),
            _ => empty[Event]
          )
          _ <- tell[Event](LogError(s"$name: Failure count reached threshold, recreating connection"))
        } yield ()
      )

      case (s, _) => (s, pure(()))
    }
}
