package com.financetracker.api.v1

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import io.circe._
import io.circe.syntax._
import fs2.Task
import fs2.interop.cats._
import scala.language.implicitConversions

import com.financetracker.api._
import com.financetracker.types._
import com.financetracker.service._
import com.financetracker.data._

object ExpensesEndpoint {

  val handler: SessionService => ExpenseService => PureEndpoint = 
    sessionService => expenseService => {
      case GET -> Root / IntVar(userId) / "expenses" / "ping" =>
        TaskAttempt.pure("pong from expenses".asJson)

      case req @ GET -> Root / IntVar(userId) / "expenses" =>
        for {
          session <- sessionService.getSessionData(req)
          expenses <- expenseService.all(UserId(userId), session)
        } yield expenses.asJson

      case req @ POST -> Root / IntVar(userId) / "expenses" =>
        for {
          session <- sessionService.getSessionData(req)
          form <- TaskAttempt.liftT(req.as(jsonOf[ExpenseForm]))
          expense <- expenseService.create(
            form.amount, 
            form.description, 
            form.comment, 
            form.occuredAt, 
            UserId(userId),
            session
          )
        } yield expense.asJson

      case req @ DELETE -> Root / IntVar(userId) / "expenses" / IntVar(expenseId) =>
        for {
          session <- sessionService.getSessionData(req)
          res <- expenseService.delete(ExpenseId(expenseId), session)
        } yield res.asJson
    }
}

case class ExpenseForm(
  amount: Amount,
  description: Description,
  comment: Option[Comment],
  occuredAt: OccuredAt
)

object ExpenseForm {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[ExpenseForm] = deriveEncoder
  implicit val decoder: Decoder[ExpenseForm] = deriveDecoder
}
