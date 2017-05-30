package com.financetracker.data

import doobie.imports.Meta
import io.circe.Encoder
import java.security.MessageDigest
import java.util.Base64

case class Password private (value: String) extends AnyVal

object Password {
  // Don't expose password to API (each json will have password filtered out)
  implicit val encodePassword: Encoder[Password] =
    Encoder.encodeString.contramap[Password](_ => "[filtered]")

  implicit val passwordMeta: Meta[Password] =
    Meta[String].xmap[Password](x => new Password(x), _.value)

  val md5Encoder = MessageDigest.getInstance("MD5")
  val base64Encoder = Base64.getEncoder

  def apply(value: String): Password = {
    new Password(base64Encoder.encodeToString(md5Encoder.digest(value.getBytes)))
  }
}
