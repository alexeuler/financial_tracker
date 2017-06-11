package com.financetracker.types

import cats.data.EitherT
import fs2.Task
import fs2.util.Attempt
import fs2.interop.cats._

object TaskAttempt {
  def apply[A](task: Task[Attempt[A]]): TaskAttempt[A] =
    EitherT[Task, Throwable, A](task)

  def lift[A](attempt: => Attempt[A]): TaskAttempt[A] =
    apply(Task.delay(attempt))

  def fail[A](error: Throwable): TaskAttempt[A] = lift[A](Left(error))

  def mapException[A](task: TaskAttempt[A])(mapper: PartialFunction[Throwable, Throwable]): TaskAttempt[A] = apply(
    task.value.map {
      case Left(e) => if (mapper.isDefinedAt(e)) Left(mapper.apply(e)) else Left(e)
      case x@Right(_) => x
    }
  )

  def liftT[A](task: Task[A]): TaskAttempt[A] =
    apply(task.attempt)

  def pure[A](value: => A): TaskAttempt[A] = lift(Right(value))
}
