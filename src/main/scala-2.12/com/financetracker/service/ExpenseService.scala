package com.financetracker.service

import cats.instances.either._
import fs2.interop.cats._
import shapeless.HList

import com.financetracker.repo._
import com.financetracker.data._
import com.financetracker.types._

trait ExpenseService {
  def all(userId: UserId, session: Session): TaskAttempt[List[Expense]]
  def create(
    amount: Amount, 
    description: Description,
    comment: Option[Comment],
    occuredAt: OccuredAt, 
    userId: UserId,
    session: Session
  ): TaskAttempt[Expense]
  def update(id: ExpenseId, values: HList, session: Session): TaskAttempt[Expense]
  def delete(id: ExpenseId, session: Session): TaskAttempt[Boolean]
}

case class ExpenseServiceImpl(expenseRepo: ExpenseRepo) extends ExpenseService {
  def all(userId: UserId, session: Session): TaskAttempt[List[Expense]] = 
    withPermissionsCheckByUserId(userId, session)(
      expenseRepo.all(userId)
    )

  def create(
    amount: Amount, 
    description: Description,
    comment: Option[Comment],
    occuredAt: OccuredAt,
    userId: UserId,
    session: Session
  ): TaskAttempt[Expense] = 
    withPermissionsCheckByUserId(userId, session)(
      expenseRepo.create(amount, description, comment, occuredAt, userId)
    ) 

  def update(id: ExpenseId, values: HList, session: Session): TaskAttempt[Expense] =
    withPermissionsCheckByExpenseId(id, session)(
      expenseRepo.update(id, values)
    )

  def delete(id: ExpenseId, session: Session): TaskAttempt[Boolean] =
    withPermissionsCheckByExpenseId(id, session)(
      expenseRepo.delete(id)
    )
  

  private def isAuthorized(userId: UserId, session: Session): Boolean =
    session match {
      case Session(_, _, Role.Admin, _) | Session(`userId`, _, Role.User, _) => true
      case _ => false
    }

  private def withPermissionsCheckByUserId[A](userId: UserId, session: Session)(task: TaskAttempt[A]): TaskAttempt[A] =
    if (isAuthorized(userId, session)) task else TaskAttempt.fail(UnauthorizedServiceException)

  private def withPermissionsCheckByExpenseId[A](expenseId: ExpenseId, session: Session)(task: TaskAttempt[A]): TaskAttempt[A] =
    for {
      maybeExpense <- expenseRepo.find(expenseId)
      expense <- maybeExpense.fold[TaskAttempt[Expense]](TaskAttempt.fail(NotFoundServiceException))(TaskAttempt.pure(_))
      res <- withPermissionsCheckByUserId(expense.userId, session)(task)
    } yield res
}