package com.financetracker.data

import doobie.imports.Meta
import io.circe.Encoder

case class Identity(value: String) extends AnyVal

object Identity {
  implicit val IdentityMeta: Meta[Identity] =
    Meta[String].xmap[Identity](x => Identity(x), _.value)
  implicit val encodeIdentity: Encoder[Identity] =
    Encoder.encodeString.contramap[Identity](_.value)
}