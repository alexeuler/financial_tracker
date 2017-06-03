package com.financetracker.service

import cats.instances.either._
import fs2.interop.cats._

import com.financetracker.repo._
import com.financetracker.data._
import com.financetracker.types._

trait UserService {
  def all(session: Session): TaskAttempt[List[User]]
  def create(provider: Provider, identity: Identity, password: Password, role: Role, session: Session): TaskAttempt[User]
  def findById(userId: UserId, session: Session): TaskAttempt[Option[User]]
  def delete(id: UserId, session: Session): TaskAttempt[Boolean]
}

case class UserServiceImpl(userRepo: UserRepo) extends UserService {
  override def all(session: Session): TaskAttempt[List[User]] = 
    withPermissionsCheck(session)(
      userRepo.all
    )
  override def create(provider: Provider, identity: Identity, password: Password, role: Role, session: Session): TaskAttempt[User] =
    withPermissionsCheck(session)(
      userRepo.create(provider, identity, password, role)
    )

  override def findById(userId: UserId, session: Session): TaskAttempt[Option[User]] =
    withPermissionsCheck(session)(
      userRepo.findById(userId)
    )

  override def delete(id: UserId, session: Session): TaskAttempt[Boolean] = 
    withPermissionsCheck(session)(
      userRepo.delete(id)
    )


  private def isAuthorized(session: Session): Boolean =
    session match {
      case Session(_, Role.Admin, _) => true
      case _ => false
    }

  private def withPermissionsCheck[A](session: Session)(task: TaskAttempt[A]): TaskAttempt[A] =
    if (isAuthorized(session)) task else TaskAttempt.fail(UnauthorizedServiceException)
}