package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class Description(value: String) extends AnyVal

object Description {
  implicit val encoder: Encoder[Description] = Encoder.encodeString.contramap[Description](_.value)
  implicit val decoder: Decoder[Description] = Decoder.decodeString.map(Description(_))

  implicit val DescriptionMeta: Meta[Description] =
    Meta[String].xmap[Description](x => Description(x), _.value)
}