package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class Identity(value: String) extends AnyVal

object Identity {
  import io.circe.generic.semiauto._

  implicit val encoder: Encoder[Identity] = deriveEncoder
  implicit val decoder: Decoder[Identity] = deriveDecoder

  implicit val IdentityMeta: Meta[Identity] =
    Meta[String].xmap[Identity](x => Identity(x), _.value)
    
}