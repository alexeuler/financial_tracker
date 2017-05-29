package com.financetracker

import cats.data.EitherT
import fs2.Task

package object types {
  type TaskAttempt[A] = EitherT[Task, Throwable, A]
}