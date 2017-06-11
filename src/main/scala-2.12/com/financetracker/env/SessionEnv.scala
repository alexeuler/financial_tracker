package com.financetracker.env

import com.typesafe.config.Config
import fs2.util.Attempt

import cats.data.ReaderT
import cats.instances.either._

import scala.concurrent.duration._

import com.financetracker.Implicits._
import com.financetracker.Implicits.Reader._
import com.financetracker.service._
import com.financetracker.repo._


object SessionEnv {
   val createSessionService: UserRepo => ReaderT[Attempt, Config, SessionService] =
    userRepo =>
      for {
        secret <- getSecret
        expiration <- getExpiration
      } yield new SessionServiceImpl(expiration, secret, userRepo)

  private def getSecret: Config => Attempt[String] = _.tryGetString("session.secret")
  private def getExpiration: Config => Attempt[Duration] = _.tryGetDuration("session.expiration")
}
