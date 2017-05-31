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
  def login(provider: Provider, identity: Identity, password: Password): TaskAttempt[Option[JWToken]]
}

case class UserServiceImpl(userRepo: UserRepo, jwtService: JWTService) extends UserService {
  override def all: TaskAttempt[List[User]] = userRepo.all
  override def create(provider: Provider, identity: Identity, password: Password, role: Role): TaskAttempt[User] =
    userRepo.create(provider, identity, password, role)

  override def findById(userId: UserId): TaskAttempt[Option[User]] =
    userRepo.findById(userId)

  override def delete(id: UserId): TaskAttempt[Boolean] = userRepo.delete(id)

  override def login(provider: Provider, identity: Identity, password: Password): TaskAttempt[Option[JWToken]] =
    for {
      maybeUser <- userRepo.find(provider, identity)
    } yield tryIssueToken(maybeUser, password)

  private def tryIssueToken(maybeUser: Option[User], password: Password): Option[JWToken] =
    for {
      user <- maybeUser
      if user.password == password
    } yield jwtService.createJWT(user)
}