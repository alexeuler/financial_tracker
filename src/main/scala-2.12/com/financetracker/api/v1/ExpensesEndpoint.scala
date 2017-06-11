package com.financetracker.api.v1

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import io.circe._
import io.circe.syntax._
import fs2.Task
import fs2.interop.cats._
import scala.language.implicitConversions
import java.sql.Timestamp
import shapeless._

import com.financetracker.api._
import com.financetracker.types._
import com.financetracker.service._
import com.financetracker.data._

object ExpensesEndpoint {

  val handler: SessionService => ExpenseService => PureEndpoint = 
    sessionService => expenseService => {
      case req @ GET -> Root / IntVar(userId) / "expenses" =>
        for {
          session <- sessionService.getSessionData(req)
          expenses <- expenseService.all(UserId(userId), session)
        } yield expenses.asJson

      case req @ GET -> Root / IntVar(userId) / "expenses" / IntVar(expenseId) =>
        for {
          session <- sessionService.getSessionData(req)
          expenses <- expenseService.find(ExpenseId(expenseId), UserId(userId), session)
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

      case req @ PATCH -> Root / IntVar(userId) / "expenses" / IntVar(expenseId) =>
        for {
          session <- sessionService.getSessionData(req)
          form <- TaskAttempt.liftT(req.as[Json].map(UpdateExpenseForm(_)))
          expense <- expenseService.update(ExpenseId(expenseId), UserId(userId), form, session)
        } yield expense.asJson

      case req @ DELETE -> Root / IntVar(userId) / "expenses" / IntVar(expenseId) =>
        for {
          session <- sessionService.getSessionData(req)
          res <- expenseService.delete(ExpenseId(expenseId), UserId(userId), session)
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

object UpdateExpenseForm {
  def apply(json: Json): HList = {
    var res: HList = HNil
    val cursor = json.hcursor
    cursor.downField("amount").as[Long].map(x => res = Amount(x) :: res)
    cursor.downField("description").as[String].map(x => res = Description(x) :: res)
    cursor.downField("comment").as[String].map(x => res = Comment(x) :: res)
    cursor.downField("occuredAt").as[Timestamp].map(x => res = OccuredAt(x) :: res)
    cursor.downField("userId").as[Int].map(x => res = UserId(x) :: res)
    res
  }
}
