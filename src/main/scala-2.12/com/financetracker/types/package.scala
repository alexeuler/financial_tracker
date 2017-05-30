package com.financetracker

import cats.data.EitherT
import fs2.Task
import org.http4s._

package object types {
  type TaskAttempt[A] = EitherT[Task, Throwable, A]
  type PureEndpoint = PartialFunction[Request, TaskAttempt[Response]]
}
