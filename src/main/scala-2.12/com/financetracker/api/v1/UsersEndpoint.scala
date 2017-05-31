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
        TaskAttempt.liftT(Ok("pong from users"))

      case GET -> Root =>
        for {
          users <- userService.all
          resp <- TaskAttempt.liftT(Ok(users.asJson))
        } yield resp

      case req@POST -> Root =>
        for {
          form <- Endpoint.requestToJson[UserForm](req)
          user <- userService.create(Provider.Email, Identity(form.email), Password(form.password), Role.Unconfirmed)
          resp <- TaskAttempt.liftT(Ok(user.asJson))
        } yield resp
    }
}

case class UserForm(email: String, password: String)

object UserForm {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[UserForm] = deriveEncoder[UserForm]
  implicit val decoder: Decoder[UserForm] = deriveDecoder[UserForm]
}