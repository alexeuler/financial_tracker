package com.financetracker.api.v1

import org.http4s._
import org.http4s.dsl._
import fs2.Task
import scala.language.implicitConversions

import com.financetracker.types._

object UsersEndpoint {
  implicit def taskToTaskAttempt[A](task: Task[A]): TaskAttempt[A] = TaskAttempt.liftT(task)

  val handler: PureEndpoint = {
    case GET -> Root / "ping" =>
      Ok("pong from users")
  }
}
