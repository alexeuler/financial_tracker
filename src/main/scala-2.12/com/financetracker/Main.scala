package com.financetracker

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import org.http4s._
import org.http4s.dsl._
import org.http4s.server.blaze._
import org.http4s.server.syntax._
import org.http4s.util.StreamApp
import com.typesafe.config.ConfigFactory
import fs2.{Stream, Task}
import fs2.interop.cats._

import env._
import types._
import api._

object Main extends StreamApp {

  def stream(args: List[String]): Stream[Task, Nothing] = {
    setLogLevel(Level.INFO)
    val app = for {
      env <- Env.createEnv(EnvType.Development)
    } yield {
      BlazeBuilder.bindHttp(env.http.port, env.http.host)
        .mountService(Endpoint.serviceFromEndpoint(api.v1.UsersEndpoint.handler(env.sessionService)(env.userService), env.logger), "/api/v1/users")
        .mountService(Endpoint.serviceFromEndpoint(api.v1.SessionsEndpoint.handler(env.sessionService), env.logger), "/api/v1/sessions")
        .serve
    }

    val conf = ConfigFactory.load()
    app.run(conf).fold(
      e => {
        println(s"Error initializing the app: $e")
        Stream.empty
      },
      identity
    )
  }

  private def setLogLevel(level: Level): Unit = {
    val root: Logger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[Logger]
    root.setLevel(level)
  }
}
