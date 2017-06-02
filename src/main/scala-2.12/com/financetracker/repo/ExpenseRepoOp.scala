package com.financetracker.repo

import doobie.imports._
import shapeless.{HList, :: => :::, HNil}
import com.financetracker.data._
import java.sql.Timestamp
import java.util.Date

trait ExpenseRepoOp {
  def all: Query0[Expense]
  def create(
    amount: Amount, 
    occuredAt: OccuredAt, 
    description: Description,
    comment: Option[Comment] = None
  ): Update0
  def update(id: ExpenseId, values: HList): Update0
  def delete(id: ExpenseId): Update0
  def deleteAll: Update0
}

object ExpenseRepoOp extends ExpenseRepoOp {
  def all: Query0[Expense] =
    sql"select * from expenses".query[Expense]

  def create(
    amount: Amount, 
    occuredAt: OccuredAt, 
    description: Description,
    comment: Option[Comment] = None
  ): Update0 =
    sql"insert into expenses (amount, occured_at, description, comment) values ($amount, $occuredAt, $description, $comment)".update

  def update(id: ExpenseId, values: HList): Update0 =
    (
      fr"update expenses set"
      ++ fieldNames(values)
      ++ fr"updated_at=${new Timestamp((new Date()).getTime)}"
      ++ fr"where id=${id}"
    ).update


  // Using only fields allowed for update here
  private def fieldNames(list: HList): Fragment = list match {
    case (x: Amount) ::: xs => 
      fr"amount=${x}," ++ fieldNames(xs)
    case (x: OccuredAt) ::: xs => 
      fr"occured_at=${x}," ++ fieldNames(xs)
    case (x: Description) ::: xs => 
      fr"description=${x}," ++ fieldNames(xs)
    case (x: Comment) ::: xs => 
      fr"comment=${x}," ++ fieldNames(xs)
    case _ => Fragment.empty
  }

  def delete(id: ExpenseId): Update0 =
    sql"delete from expenses where id=$id".update
  
  def deleteAll: Update0 =
    sql"delete from expenses".update
}
