package com.financetracker.repo

import fs2.Task
import fs2.interop.cats._
import scala.language.implicitConversions
import doobie.imports._
import shapeless.HList
import cats.data.EitherT

import com.financetracker.data._
import com.financetracker.state._
import com.financetracker.state.ConnectionState._
import com.financetracker.types._

trait UserRepo {
  def all: TaskAttempt[List[User]]
  def find(provider: Provider, identity: Identity): TaskAttempt[Option[User]]
  def findById(id: UserId): TaskAttempt[Option[User]]
  def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User]
  def update(id: UserId, values: HList): TaskAttempt[Boolean]
  def delete(id: UserId): TaskAttempt[Boolean]
  def deleteAll: TaskAttempt[Boolean]
}

case class UserRepoImpl(userRepo: UserRepoOp, stateClient: StateClient[AppState, AppState.Event]) extends UserRepo {

  implicit def connectionToTaskAttempt[A](conn: ConnectionIO[A]): TaskAttempt[A] =
    for {
      transactor <- getTransactor
      result <- TaskAttempt.liftT(conn.transact(transactor))
    } yield result

  def all: TaskAttempt[List[User]] = userRepo.all.list
  def find(provider: Provider, identity: Identity): TaskAttempt[Option[User]] = userRepo.find(provider, identity).option
  def findById(id: UserId): TaskAttempt[Option[User]] = userRepo.find(id).option
  def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User] =
    connectionToTaskAttempt(
        userRepo.create(provider, identity, password, role)
          .withUniqueGeneratedKeys("id", "provider", "identity", "password", "role", "created_at", "updated_at")
    )
  def update(id: UserId, values: HList): TaskAttempt[Boolean] = userRepo.update(id, values).run.map(_ > 0)
  def delete(id: UserId): TaskAttempt[Boolean] = userRepo.delete(id).run.map(_ > 0)
  def deleteAll: TaskAttempt[Boolean] = userRepo.deleteAll.run.map(_ > 0)

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
