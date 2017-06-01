package com.financetracker.api

import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import org.http4s.headers.Authorization
import io.circe._
import io.circe.syntax._
import io.circe.parser.decode
import fs2.Task
import fs2.util._
import com.typesafe.scalalogging.Logger

import com.financetracker.types._
import com.financetracker.data.JWToken


object Endpoint {
  def serviceFromEndpoint(endpoint: PureEndpoint, logger: Logger): HttpService =
    HttpService(
      endpoint andThen {
        x => x.value.flatMap {
          case Left(e: Exception) => 
            val (apiException, logMessage) = ApiException.fromException(e)
            logMessage.map(logger.error(_))
            BadRequest(toJsonResponse(None, Some(ApiException.toJson(apiException))))
          
          case Left(e: Throwable) =>
            logger.error(s"Unexpected throwable: $e")
            InternalServerError("Unexpected error")

          case Right(resp) => Ok(toJsonResponse(Some(resp), None))
        }
      }
    )

  def getAuthToken(request: Request): TaskAttempt[JWToken] = {
    val maybeToken = for {
      authHeader <- request.headers.get(Authorization)
      token <- authHeader.value.split(" ") match {
        case Array("Bearer", token) => Some(JWToken(token))
        case _ => None
      }
    } yield token

    maybeToken.fold[TaskAttempt[JWToken]](TaskAttempt.fail(UnauthorizedApiException))(TaskAttempt.pure(_))
  }
  
  private def toJsonResponse[A](result: Option[A], error: Option[Json])(implicit encoder: Encoder[A]): Json =
    Json.obj(
      ("error", error.asJson),
      ("result", result.asJson)
    ) 
}