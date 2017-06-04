package com.financetracker.api.v1

import org.http4s._
import org.http4s.dsl._
import java.io.File
import fs2.Task

object StaticEndpoint {
  val service = HttpService {
    case request @ GET -> Root / path if List(".js", ".css", ".map", ".html").exists(path.endsWith) =>
      StaticFile.fromResource("/" + path, Some(request)).map(Task.now).getOrElse(NotFound())
    case _ =>
      StaticFile.fromResource("/index.html").map(Task.now).getOrElse(NotFound())

  }
}