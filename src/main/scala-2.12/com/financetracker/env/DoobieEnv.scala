package com.financetracker.env

import com.typesafe.config.Config
import doobie.imports.Transactor
import doobie.hikari.hikaritransactor.HikariTransactor
import fs2.util.Attempt
import fs2.Task

import cats.data.ReaderT
import cats.instances.either._
import cats.syntax.either._

import com.financetracker.types._
import com.financetracker.Implicits._
import com.financetracker.Implicits.Reader._

case class DoobieEnv(
  maxFailures: Int, 
  createTransactor: TaskAttempt[Transactor[Task]], 
  closeTransactor: Transactor[Task] => TaskAttempt[Unit]
)

object DoobieEnv {

  val createEnv: ReaderT[Attempt, Config, DoobieEnv] =
    for {
      createTransactor <- PostgresDoobie.createDoobieTransactor
      closeTransactor <- PostgresDoobie.closeDoobieTransactor
      maxFailures <- maxDoobieFailures
    } yield DoobieEnv(maxFailures, createTransactor, closeTransactor)

  private val maxDoobieFailures: Config => Attempt[Int] = _.tryGetInt(s"db.default.maxFailures")
}

object PostgresDoobie {
  def createDoobieTransactor: Config => Attempt[TaskAttempt[Transactor[Task]]] =
    config =>
      for {
        conf <- config.tryGetConfig("db.default")
        host <- conf.tryGetString("host")
        port <- conf.tryGetInt("port")
        database <- conf.tryGetString("database")
        user <- conf.tryGetString("user")
        password <- conf.tryGetString("password")
      } yield TaskAttempt(
        HikariTransactor[Task](
          "org.postgresql.Driver",
          s"jdbc:postgresql://$host:$port/$database",
          user,
          password
        ).attempt
      )
  
  def closeDoobieTransactor: Config => Attempt[Transactor[Task] => TaskAttempt[Unit]] =
    // Ok to typecast here, since we know that createDoobieTransactor returns HikariTransactor
    _ => Right(trans => TaskAttempt(trans.asInstanceOf[HikariTransactor[Task]].shutdown.attempt))
}