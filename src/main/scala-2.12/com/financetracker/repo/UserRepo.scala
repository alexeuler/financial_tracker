package com.financetracker.repo

import cats.~>
import fs2.Task
import fs2.interop.cats._
import doobie.imports._
import shapeless.HList
import cats.data.EitherT
import scala.language.implicitConversions

import com.financetracker.data._
import com.financetracker.types._

trait UserRepo {
  def all: TaskAttempt[List[User]]
  def find(provider: Provider, identity: Identity): TaskAttempt[Option[User]]
  def findById(id: UserId): TaskAttempt[Option[User]]
  def count: TaskAttempt[Long]
  def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User]
  def update(id: UserId, values: HList): TaskAttempt[User]
  def delete(id: UserId): TaskAttempt[Boolean]
  def deleteAll: TaskAttempt[Int]
}

case class UserRepoImpl(userRepo: UserRepoOp, run: ConnectionIO ~> TaskAttempt) extends UserRepo {

  implicit def connToTask[A](conn: ConnectionIO[A]): TaskAttempt[A] = run(conn)

  def all: TaskAttempt[List[User]] = userRepo.all.list
  def find(provider: Provider, identity: Identity): TaskAttempt[Option[User]] = userRepo.find(provider, identity).option
  def findById(id: UserId): TaskAttempt[Option[User]] = userRepo.find(id).option
  def count: TaskAttempt[Long] = userRepo.count.unique
  def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User] =
    connToTask(
        userRepo.create(provider, identity, password, role)
          .withUniqueGeneratedKeys("id", "provider", "identity", "password", "role", "created_at", "updated_at")
    )
  def update(id: UserId, values: HList): TaskAttempt[User] = 
    connToTask(
      userRepo.update(id, values)
        .withUniqueGeneratedKeys("id", "provider", "identity", "password", "role", "created_at", "updated_at")
    )
  def delete(id: UserId): TaskAttempt[Boolean] = userRepo.delete(id).run.map(_ > 0)
  def deleteAll: TaskAttempt[Int] = userRepo.deleteAll.run
}
