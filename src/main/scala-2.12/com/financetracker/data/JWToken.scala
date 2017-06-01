package com.financetracker.data

import io.circe._

case class JWToken(value: String) extends AnyVal

object JWToken {
  implicit val encoder: Encoder[JWToken] = Encoder.encodeString.contramap[JWToken](_.value)
  implicit val decoder: Decoder[JWToken] = Decoder.decodeString.map(JWToken(_))

}
