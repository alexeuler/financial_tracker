package com.financetracker.api

object ApiException {
  case class MalformedJson(message: String, error: Throwable) extends Exception
  case class BadTypeJson(message: String) extends Exception
  case class UnknownFailure(message: String, error: Throwable) extends Exception
}
