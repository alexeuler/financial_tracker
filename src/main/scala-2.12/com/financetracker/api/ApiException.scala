package com.financetracker.api

import io.circe._
import io.circe.syntax._

import com.financetracker.service._

sealed trait ApiException extends Exception
case object NotFoundException extends ApiException

case object MalformedJsonException extends ApiException
case object BadTypeJsonException extends ApiException
case object UnknownJsonException extends ApiException

case object UnauthorizedApiException extends ApiException
case object OutdatedTokenApiException extends ApiException
case object UserAlreadyExistsApiException extends ApiException
case object InvalidEmailApiException extends ApiException

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

    case UnauthorizedApiException =>
      Json.obj(
        ("code", 300.asJson), 
        ("message", "Unauthorized".asJson)
      )

    case OutdatedTokenApiException =>
      Json.obj(
        ("code", 301.asJson), 
        ("message", "Token has expired".asJson)
      )

    case UserAlreadyExistsApiException =>
      Json.obj(
        ("code", 302.asJson), 
        ("message", "User with this email already exists".asJson)
      )

    case InvalidEmailApiException =>
      Json.obj(
        ("code", 303.asJson), 
        ("message", "Invalid email".asJson)
      )

    case _ => Json.obj(
      ("code", 999.asJson), 
      ("message", "Unknown error".asJson)
    )
  }

  def fromException(e: Exception): (ApiException, Option[String]) = e match {
      case e: ApiException => (e, None)
      case org.http4s.MalformedMessageBodyFailure(message, err) => (MalformedJsonException, Some(s"Malformed Json: $message: $err"))
      case e:java.text.ParseException => (MalformedJsonException, Some(s"Malformed Json: $e"))
      case org.http4s.InvalidMessageBodyFailure(message, e) => (BadTypeJsonException, Some(s"Bad type Json: $message: $e"))
      case e: org.http4s.DecodeFailure => (UnknownJsonException, Some(s"Unknown Json failure: $e"))
      case UnauthorizedServiceException => (UnauthorizedApiException, None)
      case NotFoundServiceException => (NotFoundException, None)
      case UserAlreadyExistsServiceException => (UserAlreadyExistsApiException, None)
      case InvalidEmailServiceException => (InvalidEmailApiException, None)
      case OutdatedTokenServiceException => (OutdatedTokenApiException, None)
      case e => (UnknownException, Some(s"Unknown failure: $e"))
  }
}
