package com.financetracker.api

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import fs2.Task
import fs2.util._
import com.financetracker.types._

object Endpoint {
  def serviceFromEndpoint(endpoint: PureEndpoint): HttpService =
    HttpService(
      endpoint andThen {
        x => x.value.flatMap {
          case Left(e: ApiException) => BadRequest(toJsonResponse(None, Some(ApiException.toJson(e))))
          case Left(e) => InternalServerError(toJsonResponse(None, Some(ApiException.toJson(UnknownFailure("", e)))))
          case Right(resp) => Ok(toJsonResponse(Some(resp)))
        }
      }
    )

  def requestToJson[A](req: Request)(implicit decoder: Decoder[A]): TaskAttempt[A] =
    TaskAttempt(
      req.as[String].map(decodeJsonBody[A](_))
    )
  
  private def toJsonResponse[A](result: Option[A], error: Option[Json] = None)(implicit encoder: Encoder[A]): Json =
    Json.obj(
      ("error", error.asJson),
      ("result", result.asJson)
    ) 

  private def decodeJsonBody[A](body: String)(implicit decoder: Decoder[A]): Attempt[A] = 
    circeJsonToAttempt(body, decode[A](body))
    
  private def circeJsonToAttempt[A](body: String, value: Either[io.circe.Error, A]): Attempt[A] = value match {
    case Left(ParsingFailure(message, e)) =>
      Left(MalformedJson(s"Error parsing body: $body. $message", e))
    case Left(DecodingFailure(message, _)) =>
      Left(BadTypeJson(s"Error converting json to expected type: $body. $message"))
    case Left(e: io.circe.Error) =>
      Left(UnknownFailure(body, e))
    case v@Right(_) => v
  }
}