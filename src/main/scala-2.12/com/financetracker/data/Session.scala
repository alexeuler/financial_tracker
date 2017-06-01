package com.financetracker.data

import io.circe._

case class Session(provider: Provider, identity: Identity, role: Role, expires: Long)

object Session {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[Session] = deriveEncoder
  implicit val decoder: Decoder[Session] = deriveDecoder
}
