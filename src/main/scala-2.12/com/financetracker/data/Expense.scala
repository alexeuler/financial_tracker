package com.financetracker.data

import java.sql.Timestamp
import io.circe._

case class Expense(
  id: ExpenseId,
  amount: Amount,
  occuredAt: OccuredAt,
  description: Description,
  comment: Option[Comment],
  userId: UserId,
  createdAt: Timestamp,
  updatedAt: Timestamp
)

object Expense {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[User] = deriveEncoder[User]
}