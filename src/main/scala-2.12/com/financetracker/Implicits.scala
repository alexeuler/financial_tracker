package com.financetracker
import cats.data.{Reader, ReaderT}
import fs2.util.Attempt
import com.typesafe.config.Config
import util.control.NonFatal

import cats.syntax.either._

import collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.implicitConversions

object Implicits {
  object Reader {
    implicit def funcToReaderAttempt[I, O](func: I => Attempt[O]): ReaderT[Attempt, I, O] = new ReaderT(func)
    implicit def readerToFuncAttempt[I, O](reader: ReaderT[Attempt, I, O]): I => Attempt[O] = reader.run
  }

  implicit class ScalaConfig(config: Config) {
    def tryGetInt(path: String): Attempt[Int] = 
      attempt(config.getInt(path))
      
    def tryGetString(path: String): Attempt[String] = 
      attempt(config.getString(path))

    def tryGetBoolean(path: String): Attempt[Boolean] =
      attempt(config.getBoolean(path))

    def tryGetDuration(path: String): Attempt[Duration] =
      attempt(Duration.fromNanos(config.getDuration(path).toNanos))

    def tryGetConfig(path: String): Attempt[Config] =
      attempt(config.getConfig(path))

    def tryGetConfigList(path: String): Attempt[List[Config]] =
      attempt(config.getConfigList(path).asScala.toList)
  }

  private def attempt[A](block: => A): Attempt[A] =
    try {
      Right(block)
    } catch {
      case NonFatal(e) => Left(e)
    }
}
