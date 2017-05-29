package com.financetracker.state

import com.typesafe.scalalogging.Logger
import fs2.Task
import fs2.interop.cats._
import StateFold._

import com.financetracker.types._
import com.financetracker.env.DoobieEnv

case class AppState(doobieConn: ConnectionState.State)

object AppState {
  sealed trait Event
  case class LogInfo(message: String) extends Event
  case class LogWarn(message: String) extends Event
  case class LogError(message: String) extends Event
  case class DoobieEvent(e: ConnectionState.Event) extends Event

  def fold: DoobieEnv => Logger => StateFold[AppState, Event] =
    doobieEnv => logger => StateFold[AppState, Event] {
      case (state, LogInfo(message)) =>
        logger.info(message)
        (state, empty)

      case (state, LogWarn(message)) =>
        logger.warn(message)
        (state, empty)

      case (state, LogError(message)) =>
        logger.error(message)
        (state, empty)

      case (state, DoobieEvent(event)) =>
        val doobieFold = ConnectionState.fold("Doobie")(doobieEnv.maxFailures)(doobieEnv.createTransactor)(doobieEnv.closeTransactor)
        val (doobieState, events) = doobieFold.run(state.doobieConn, event)
        val newState = if (doobieState == state.doobieConn) state else state.copy(doobieConn = doobieState)
        (newState, events.mapWritten[List[Event]](_.map {
          case ConnectionState.LogInfo(msg) => LogInfo(msg)
          case ConnectionState.LogError(msg) => LogError(msg)
          case e => DoobieEvent(e)
        }))
    }

  def initial: AppState = AppState(ConnectionState.Empty)
}