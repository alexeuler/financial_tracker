package com.financetracker.repo

import doobie.imports._
import cats.~>
import fs2.Task
import fs2.interop.cats._

import com.financetracker.state._
import com.financetracker.state.ConnectionState._
import com.financetracker.types._

object Repo {

  val run: StateClient[AppState, AppState.Event] => ConnectionIO ~> TaskAttempt =
    stateClient => new (ConnectionIO ~> TaskAttempt) {
      def apply[A](conn: ConnectionIO[A]): TaskAttempt[A] =
        for {
          transactor <- getTransactor
          result <- TaskAttempt.liftT(conn.transact(transactor))
        } yield result

      private def getTransactor: TaskAttempt[Transactor[Task]] =
      stateClient.get.doobieConn match {
        case Cached(transactor: Transactor[_], _) => TaskAttempt.pure(transactor.asInstanceOf[Transactor[Task]])
        case _ =>
          TaskAttempt(
            stateClient.dispatch(AppState.DoobieEvent(ConnectionState.CreateRequest))
              .map(_ => stateClient.get.doobieConn match {
                case Cached(transactor: Transactor[_], _) => Right(transactor.asInstanceOf[Transactor[Task]])
                case _ => Left(new Exception("State: Doobie: Unable to retrieve Hikari transactor"))
              })
          )
      }
    }
}
