package com.financetracker.api.v1

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import io.circe._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import fs2.Task
import fs2.interop.cats._

import com.financetracker.api._
import com.financetracker.types._
import com.financetracker.service._
import com.financetracker.data._

object SessionsEndpoint {

  val handler: SessionService => PureEndpoint = 
    sessionService => {
      case req @ POST -> Root =>
        for {
          login <- TaskAttempt.liftT(req.as(jsonOf[Login]))
          token <- sessionService.login(Identity(login.email), Password(login.password))
        } yield token.asJson
    }

  private case class Login(email: String, password: String)

  private object Login {
    import io.circe.generic.semiauto._
    implicit val encoder: Encoder[Login] = deriveEncoder
    implicit val decoder: Decoder[Login] = deriveDecoder
  }
}
