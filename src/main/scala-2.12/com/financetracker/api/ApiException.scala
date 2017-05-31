package com.financetracker.api

import io.circe._
import io.circe.syntax._

sealed trait ApiException extends Exception
case object NotFoundException extends ApiException
case object MalformedJsonException extends ApiException
case object BadTypeJsonException extends ApiException
case object UnknownJsonException extends ApiException
case object UnknownException extends ApiException


object ApiException {

  def toJson(exception: ApiException): Json = exception match {
    case NotFoundException => Json.obj(("code", 100.asJson), ("message", "Not Found".asJson))
    case MalformedJsonException => 
      Json.obj(
        ("code", 200.asJson), 
        ("message", "Malformed json body in request".asJson)
      )
    case BadTypeJsonException =>
      Json.obj(
        ("code", 201.asJson), 
        ("message", "Json structure doesn't match expected type".asJson)
      )
    case UnknownJsonException =>
      Json.obj(
        ("code", 299.asJson), 
        ("message", "Unknown error while converting Json".asJson)
      )
    case _ => Json.obj(
      ("code", 999.asJson), 
      ("message", "Unknown error".asJson)
    )
  }

  def fromException(e: Exception): (ApiException, Option[String]) = e match {
      case e: ApiException => (e, None)
      case org.http4s.MalformedMessageBodyFailure(message, err) => (MalformedJsonException, Some(s"Malformed Json: $message: $err"))
      case org.http4s.InvalidMessageBodyFailure(message, e) => (BadTypeJsonException, Some(s"Bad type Json: $message: $e"))
      case e: io.circe.Error => (UnknownJsonException, Some(s"Unknown Json failure: $e"))
      case e => (UnknownException, Some(s"Unknown failure: $e"))
  }
}

