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

  val handler: SessionService => UserService => PureEndpoint = 
    sessionService => userService => {
      case GET -> Root / "ping" =>
        TaskAttempt.pure("pong from users".asJson)

      case req @ GET -> Root =>
        for {
          token <- Endpoint.getAuthToken(req)
          _ <- sessionService.authorize(token, Role.Admin, Role.Manager)
          users <- userService.all
        } yield users.asJson

      case req @ GET -> Root / IntVar(userId) =>
        for {
          token <- Endpoint.getAuthToken(req)
          _ <- sessionService.authorize(token, Role.Admin, Role.Manager)
          maybeUser <- userService.findById(UserId(userId))
          user <- maybeUser.fold[TaskAttempt[User]](TaskAttempt.fail(NotFoundException))(u => TaskAttempt.pure(u))
        } yield user.asJson

      case req @ POST -> Root =>
        for {
          token <- Endpoint.getAuthToken(req)
          _ <- sessionService.authorize(token, Role.Admin, Role.Manager)
          form <- TaskAttempt.liftT(req.as(jsonOf[UserForm]))
          user <- userService.create(Provider.Email, form.email, form.password, form.role)
        } yield user.asJson

      case req @ DELETE -> Root / IntVar(userId) =>
        for {
          token <- Endpoint.getAuthToken(req)
          _ <- sessionService.authorize(token, Role.Admin, Role.Manager)
          res <- userService.delete(UserId(userId))
        } yield res.asJson
    }
}

case class UserForm(email: Identity, password: Password, role: Role)

object UserForm {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[UserForm] = deriveEncoder
  implicit val decoder: Decoder[UserForm] = deriveDecoder
}
