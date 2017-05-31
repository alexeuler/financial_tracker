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

object UsersEndpoint {

  val handler: UserService => PureEndpoint = 
    userService => {
      case GET -> Root / "ping" =>
        TaskAttempt.pure("pong from users".asJson)

      case GET -> Root =>
        for {
          users <- userService.all
        } yield users.asJson

      case GET -> Root / IntVar(userId) =>
        for {
          maybeUser <- userService.findById(UserId(userId))
          user <- maybeUser.fold[TaskAttempt[User]](TaskAttempt.fail(NotFoundException))(u => TaskAttempt.pure(u))
        } yield user.asJson

      case req@POST -> Root =>
        for {
          form <- TaskAttempt.liftT(req.as(jsonOf[UserForm]))
          user <- userService.create(Provider.Email, Identity(form.email), Password(form.password), Role.Unconfirmed)
        } yield user.asJson

      case DELETE -> Root / IntVar(userId) =>
        for {
          res <- userService.delete(UserId(userId))
        } yield res.asJson
    }
}

case class UserForm(email: String, password: String)

object UserForm {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[UserForm] = deriveEncoder[UserForm]
  implicit val decoder: Decoder[UserForm] = deriveDecoder[UserForm]
}
