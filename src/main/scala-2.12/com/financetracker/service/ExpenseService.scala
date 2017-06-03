package com.financetracker.service

import cats.instances.either._
import fs2.interop.cats._
import shapeless.HList

import com.financetracker.repo._
import com.financetracker.data._
import com.financetracker.types._

trait ExpenseService {
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
}

case class ExpenseServiceImpl(expenseRepo: ExpenseRepo) extends ExpenseService {
  def all(userId: UserId): TaskAttempt[List[Expense]] = expenseRepo.all(userId)
  def find(expenseId: ExpenseId): TaskAttempt[Option[Expense]] = expenseRepo.find(expenseId)
  def create(
    amount: Amount, 
    description: Description,
    comment: Option[Comment],
    occuredAt: OccuredAt, 
    userId: UserId
  ): TaskAttempt[Expense] = expenseRepo.create(amount, description, comment, occuredAt, userId)
  def update(id: ExpenseId, values: HList): TaskAttempt[Expense] = expenseRepo.update(id, values)
  def delete(id: ExpenseId): TaskAttempt[Boolean] = expenseRepo.delete(id)
}