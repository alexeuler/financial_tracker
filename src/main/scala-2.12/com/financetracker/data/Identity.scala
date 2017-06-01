package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class Identity(value: String) extends AnyVal

object Identity {
  implicit val encoder: Encoder[Identity] = Encoder.encodeString.contramap[Identity](_.value)
  implicit val decoder: Decoder[Identity] = Decoder.decodeString.map(Identity(_))

  implicit val IdentityMeta: Meta[Identity] =
    Meta[String].xmap[Identity](x => Identity(x), _.value)
    
}