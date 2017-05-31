package com.financetracker.api

import io.circe._
import io.circe.syntax._

sealed trait ApiException extends Exception
case object EntityNotFound extends ApiException
case class MalformedJson(message: String, error: Throwable) extends ApiException
case class BadTypeJson(message: String) extends ApiException
case class UnknownFailure(message: String, error: Throwable) extends ApiException


object ApiException {
  def toJson(exception: ApiException): Json = exception match {
    case EntityNotFound => Json.obj(("code", 100.asJson), ("message", "Not Found".asJson))
    case MalformedJson(_, _) => 
      Json.obj(
        ("code", 200.asJson), 
        ("message", "Malformed json body in request".asJson)
      )
    case BadTypeJson(_) =>
      Json.obj(
        ("code", 201.asJson), 
        ("message", "Json structire doesn't match expected type".asJson)
      )
    case _ => Json.obj(
      ("code", 900.asJson), 
      ("message", "Unknown error".asJson)
    )
  }
}
