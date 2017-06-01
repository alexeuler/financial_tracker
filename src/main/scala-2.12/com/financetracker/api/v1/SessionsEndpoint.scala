package com.financetracker.api.v1

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import io.circe._
import io.circe.syntax._
import fs2.Task
import fs2.interop.cats._

import com.financetracker.api._
import com.financetracker.types._
import com.financetracker.service._
import com.financetracker.data._

object SessionsEndpoint {

  val handler: SessionService => PureEndpoint = 
    sessionService => {
      case GET -> Root / "ping" =>
        TaskAttempt.pure("pong from sessions".asJson)
    }
}
