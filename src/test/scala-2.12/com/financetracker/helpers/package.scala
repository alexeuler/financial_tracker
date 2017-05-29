package com.financetracker

import doobie.imports._
import com.typesafe.config.{Config, ConfigFactory}
import cats.syntax.either._
import fs2.util.Attempt
import fs2.Task

import concurrent.Await
import concurrent.duration._

import com.financetracker.env._
import com.financetracker.Implicits._
import com.financetracker.Implicits.Reader._

package object helpers {

  val config = ConfigFactory.load()
  val env: Env = Env.createEnv(EnvType.Test).run(config).fold(
      e => throw new Exception(s"Error initializing the env from config in test helpers: $e"),
      identity
    )

  val lightTransactorReader: Config => Attempt[doobie.imports.Transactor[IOLite]] =
    conf => for {
      database <- conf.tryGetString("db.default.database")
      user <- conf.tryGetString("db.default.user")
      password <- conf.tryGetString("db.default.password")   
    } yield DriverManagerTransactor[IOLite](
        "org.postgresql.Driver",
        s"jdbc:postgresql:$database", 
        user,
        password
    )

  val lightTransactor =
    lightTransactorReader(config).right.getOrElse({
      throw new Exception(
        s"Error creating Doobie transactor for tests: ${lightTransactorReader(config).left.get}"
      )
    })
}
