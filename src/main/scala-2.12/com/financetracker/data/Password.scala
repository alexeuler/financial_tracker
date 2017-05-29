package com.financetracker.data

import doobie.imports.Meta
import io.circe.Encoder

case class Password(value: String) extends AnyVal

object Password {
  implicit val encodePassword: Encoder[Password] =
    Encoder.encodeString.contramap[Password](_.value)

  implicit val passwordMeta: Meta[Password] =
    Meta[String].xmap[Password](x => Password(x), _.value)
}
