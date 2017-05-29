package com.financetracker.state

import fs2.interop.cats._
import fs2.{Strategy, Task}

trait StateClient[S, E] {
  def get: S
  def dispatch: E => Task[Unit]
}

case class StateClientImpl[S, E](initial: S, fold: StateFold[S, E]) extends StateClient[S, E] {
  override def get: S = state

  override def dispatch: E => Task[Unit] =
    event => Task.delay({
      val (st, effects) = fold.run(state, event)
      state = st
      effects.written
    })
      .async
      .flatMap(identity)
      .flatMap(Task.traverse(_)(e => dispatch(e)))
      .map(_ => ())

  implicit val strategy: Strategy = Strategy.fromFixedDaemonPool(1)

  @volatile private var state: S = initial
}