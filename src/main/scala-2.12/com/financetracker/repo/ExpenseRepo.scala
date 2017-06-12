package com.financetracker.repo

import cats.~>
import doobie.imports._
import shapeless.{HList, :: => :::, HNil}
import com.financetracker.data._
import java.sql.Timestamp
import java.util.Date
import scala.language.implicitConversions

import com.financetracker.types._

trait ExpenseRepo {
  def all(userId: UserId): TaskAttempt[List[Expense]]
  def find(expenseId: ExpenseId): TaskAttempt[Option[Expense]]
  def create(
    amount: Amount, 
    description: Description,
    comment: Option[Comment],
    occuredAt: OccuredAt, 
    userId: UserId
  ): TaskAttempt[Expense]
  def update(id: ExpenseId, values: HList): TaskAttempt[Expense]
  def delete(id: ExpenseId): TaskAttempt[Boolean]
  def deleteAllForUser(id: UserId): TaskAttempt[Int]
  def deleteAll: TaskAttempt[Int]
}

case class ExpenseRepoImpl(expenseRepoOp: ExpenseRepoOp, run: ConnectionIO ~> TaskAttempt) extends ExpenseRepo {

  implicit def connToTask[A](conn: ConnectionIO[A]): TaskAttempt[A] = run(conn)

  def all(userId: UserId): TaskAttempt[List[Expense]] =
    expenseRepoOp.all(userId).list

  def find(expenseId: ExpenseId): TaskAttempt[Option[Expense]] =
    expenseRepoOp.find(expenseId).option

  def create(
    amount: Amount, 
    description: Description,
    comment: Option[Comment],
    occuredAt: OccuredAt, 
    userId: UserId    
  ): TaskAttempt[Expense] =
    connToTask(
      expenseRepoOp.create(amount, description, comment, occuredAt, userId)
        .withUniqueGeneratedKeys("id", "amount", "description", "comment", "occured_at", "user_id", "created_at", "updated_at")
    )

  def update(id: ExpenseId, values: HList): TaskAttempt[Expense] =
    connToTask(
      expenseRepoOp.update(id, values)
        .withUniqueGeneratedKeys("id", "amount", "description", "comment", "occured_at", "user_id", "created_at", "updated_at")
    )

  def delete(id: ExpenseId): TaskAttempt[Boolean] =
    expenseRepoOp.delete(id).run.map(_ > 0)

  def deleteAllForUser(id: UserId): TaskAttempt[Int] =
    expenseRepoOp.deleteAllForUser(id).run

  def deleteAll: TaskAttempt[Int] = expenseRepoOp.deleteAll.run

}
