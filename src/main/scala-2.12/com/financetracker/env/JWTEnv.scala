package com.financetracker.env

import com.typesafe.config.Config
import fs2.util.Attempt

import cats.data.ReaderT
import cats.instances.either._

import scala.concurrent.duration._

import com.financetracker.Implicits._
import com.financetracker.Implicits.Reader._
import com.financetracker.service._


object JWTEnv {
   val createJWTService: ReaderT[Attempt, Config, JWTService] =
    for {
      key <- getKey
      expiration <- getExpiration
    } yield new JWTServiceImpl(expiration, key)

  private def getKey: Config => Attempt[String] = _.tryGetString("jwt.key")
  private def getExpiration: Config => Attempt[Duration] = _.tryGetDuration("jwt.expiration")
}