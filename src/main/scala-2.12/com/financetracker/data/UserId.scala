package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class UserId(value: Int) extends AnyVal

object UserId {
  implicit val encoder: Encoder[UserId] = Encoder.encodeInt.contramap[UserId](_.value)
  implicit val decoder: Decoder[UserId] = Decoder.decodeInt.map(UserId(_))

  implicit val idMeta: Meta[UserId] =
    Meta[Int].xmap[UserId](x => UserId(x), _.value)
}
