package com.financetracker.env

import com.typesafe.config.Config
import fs2.util.Attempt

import cats.data.ReaderT
import cats.instances.either._

import com.financetracker.Implicits._
import com.financetracker.Implicits.Reader._

case class HttpEnv(host: String, port: Int)

object HttpEnv {
  val createEnv: ReaderT[Attempt, Config, HttpEnv] =
    for {
      host <- getHost
      port <- getPort
    } yield HttpEnv(host, port)

  private def getPort: Config => Attempt[Int] = _.tryGetInt("http.port")
  private def getHost: Config => Attempt[String] = _.tryGetString("http.host")
}
