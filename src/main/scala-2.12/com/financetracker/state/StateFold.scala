package com.financetracker.state

import cats.data.WriterT
import fs2.Task
import cats.implicits._
import fs2.interop.cats._
import fs2.Strategy

case class StateFold[S, E](run: (S, E) => (S, WriterT[Task, List[E], Unit]))

object StateFold {
  def empty[E]: WriterT[Task, List[E], Unit] = pure(())
  def pure[E, A](a: A): WriterT[Task, List[E], A] = WriterT.lift[Task, List[E], A](Task.now(a))
  def pureTask[E, A](a: Task[A]): WriterT[Task, List[E], A] = WriterT.lift[Task, List[E], A](a)
  def tell[E](events: E*): WriterT[Task, List[E], Unit] = WriterT.tell[Task, List[E]](events.toList)
  def traverse[A, E](list: List[A])(f: A => WriterT[Task, List[E], Unit]): WriterT[Task, List[E], Unit] =
    for {
      events <- pureTask(
        Task.traverse(list.map(f andThen (w => w.run.map(_._1))))(identity).map(_.flatten.toList)
      )
      _ <- tell[E](events:_*)
    } yield ()

  def parallelTraverse[A, E](list: List[A])(f: A => WriterT[Task, List[E], Unit])(implicit S: Strategy): WriterT[Task, List[E], Unit] =
    for {
      events <- pureTask(
        Task.parallelTraverse(list.map(f andThen (w => w.run.map(_._1))))(identity).map(_.flatten.toList)
      )
      _ <- tell[E](events:_*)
    } yield ()
}
