package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class Amount(value: Long) extends AnyVal

object Amount {
  implicit val encoder: Encoder[Amount] = Encoder.encodeLong.contramap[Amount](_.value)
  implicit val decoder: Decoder[Amount] = Decoder.decodeLong.map(Amount(_))

  implicit val amountMeta: Meta[Amount] =
    Meta[Long].xmap[Amount](x => Amount(x), _.value)   
}