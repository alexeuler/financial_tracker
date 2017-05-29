package com.financetracker.data

import doobie.imports.Meta
import io.circe.Encoder

case class UserId(value: Int) extends AnyVal

object UserId {
  implicit val encodeUserId: Encoder[UserId] =
    Encoder.encodeInt.contramap[UserId](_.value)

  implicit val idMeta: Meta[UserId] =
    Meta[Int].xmap[UserId](x => UserId(x), _.value)
}
