package com.financetracker.service

import cats.instances.either._
import fs2.interop.cats._

import com.financetracker.repo._
import com.financetracker.data._
import com.financetracker.types._

trait UserService {
  def all: TaskAttempt[List[User]]
  def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User]
  def findById(userId: UserId): TaskAttempt[Option[User]]
  def delete(id: UserId): TaskAttempt[Boolean]
}

case class UserServiceImpl(userRepo: UserRepo) extends UserService {
  override def all: TaskAttempt[List[User]] = userRepo.all
  override def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User] =
    userRepo.create(provider, identity, password, role)

  override def findById(userId: UserId): TaskAttempt[Option[User]] =
    userRepo.findById(userId)

  override def delete(id: UserId): TaskAttempt[Boolean] = userRepo.delete(id)
}