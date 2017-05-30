package com.financetracker.api

import org.http4s._
import org.http4s.dsl._
import fs2.Task
import com.financetracker.types._

object Endpoint {
  def serviceFromEndpoint(endpoint: PureEndpoint): HttpService =
    HttpService(
      endpoint andThen {
        x => x.value.flatMap {
          // case Left(HttpFailure.MalformedJson(message, _)) => UnprocessableEntity(s"Malformed Json: $message")
          // case Left(HttpFailure.BadTypeJson(message)) => UnprocessableEntity(s"Json doesn't match expected structure: $message")
          case Left(e) => InternalServerError("Unknown error")
          case Right(resp) => Task.now(resp)
        }
      }
    )
}