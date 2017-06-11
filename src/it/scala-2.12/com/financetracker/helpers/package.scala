package com.financetracker

import org.http4s.Uri
import org.http4s.client.blaze._
import fs2.Task
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

import com.typesafe.config._
import com.financetracker.env._

package object helpers {
  lazy val httpClient = PooledHttp1Client()
  lazy val baseUrl: Uri = Uri.fromString(s"http://${httpEnv.host}:${httpEnv.port}/api/v1").right.get

  def async[A](task: Task[A]): A = Await.result(task.unsafeRunAsyncFuture, timeout)
  private lazy val config = ConfigFactory.load()
  private lazy val httpEnv = HttpEnv.createEnv.run(config).right.get
  private val timeout = 10000.millis
}
