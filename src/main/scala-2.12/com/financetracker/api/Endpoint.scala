package com.financetracker.api

import org.http4s._
import org.http4s.dsl._
import io.circe._
import io.circe.parser.decode
import fs2.Task
import fs2.util._
import com.financetracker.types._

object Endpoint {
  def serviceFromEndpoint(endpoint: PureEndpoint): HttpService =
    HttpService(
      endpoint andThen {
        x => x.value.flatMap {
          case Left(ApiException.MalformedJson(message, _)) => UnprocessableEntity(s"Malformed Json: $message")
          case Left(ApiException.BadTypeJson(message)) => UnprocessableEntity(s"Json doesn't match expected structure: $message")
          case Left(e) => InternalServerError("Unknown error")
          case Right(resp) => Task.now(resp)
        }
      }
    )

  def requestToJson[A](req: Request)(implicit decoder: Decoder[A]): TaskAttempt[A] =
    TaskAttempt(
      req.as[String].map(decodeJsonBody(_))
    )

  private def decodeJsonBody[A](body: String)(implicit decoder: Decoder[A]): Attempt[A] = 
    circeJsonToAttempt(body, decode[A](body))
    

  private def circeJsonToAttempt[A](body: String, value: Either[io.circe.Error, A]): Attempt[A] = value match {
    case Left(ParsingFailure(message, e)) =>
      Left(ApiException.MalformedJson(s"Error parsing body: $body. $message", e))
    case Left(DecodingFailure(message, _)) =>
      Left(ApiException.BadTypeJson(s"Error converting json to expected type: $body. $message"))
    case Left(e: io.circe.Error) =>
      Left(ApiException.UnknownFailure(body, e))
    case v@Right(_) => v
  }
}