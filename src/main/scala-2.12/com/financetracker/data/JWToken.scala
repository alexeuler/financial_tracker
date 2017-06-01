package com.financetracker.data

import io.circe._

case class JWToken(value: String) extends AnyVal

object JWToken {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[JWToken] = deriveEncoder
  implicit val decoder: Decoder[JWToken] = deriveDecoder
}
