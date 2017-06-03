package com.financetracker.env

import doobie.imports._
import com.typesafe.config.Config
import com.typesafe.scalalogging.Logger
import fs2.Task
import fs2.util.Attempt
import cats.data.ReaderT
import cats.instances.either._
import cats.~>

import com.financetracker.state._
import com.financetracker.service._
import com.financetracker.repo._
import com.financetracker.types._

case class Env(
  stateClient: StateClient[AppState, AppState.Event], 
  doobie: DoobieEnv,
  http: HttpEnv,
  userService: UserService,
  sessionService: SessionService,
  expenseService: ExpenseService,
  logger: Logger
)

object Env {
  def createEnv(envType: EnvType): ReaderT[Attempt, Config, Env] = for {
    doobie <- DoobieEnv.createEnv
    http <- HttpEnv.createEnv
    logger = Logger("com.financetracker")
    stateClient = StateClientImpl(AppState.initial, AppState.fold(doobie)(logger))
    runDoobie = Repo.run(stateClient)
    userRepo = UserRepoImpl(UserRepoOp, runDoobie)
    expenseRepo = ExpenseRepoImpl(ExpenseRepoOp, runDoobie)
    sessionService <- SessionEnv.createSessionService(userRepo)
    userService = UserServiceImpl(userRepo)
    expenseService = ExpenseServiceImpl(expenseRepo)
  } yield Env(stateClient, doobie, http, userService, sessionService, expenseService, logger)
}
