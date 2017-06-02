package com.financetracker

import java.sql.Timestamp

import doobie.imports.Meta
import io.circe._
import scala.util.Try
import java.text.SimpleDateFormat


package object data {
  type Provider = Provider.Value
  type Role = Role.Value

  val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  implicit val encodeProvider: Encoder[Provider] =
    Encoder.encodeString.contramap[Provider](_.toString)
  implicit val decodeProvider: Decoder[Provider] = Decoder.enumDecoder(Provider)

  implicit val encodeRole: Encoder[Role] =
    Encoder.encodeString.contramap[Role](_.toString)
  implicit val decodeRole: Decoder[Role] = Decoder.enumDecoder(Role)

  implicit val encodeTimestamp: Encoder[Timestamp] =
    Encoder.encodeString.contramap[Timestamp](dateFormat.format(_))
  implicit val decodeTimestamp: Decoder[Timestamp] =
    Decoder.decodeString.map(str => new Timestamp(dateFormat.parse(str).getTime))

  implicit val providerMeta: Meta[Provider] =
    Meta[String].xmap[Provider](
      str => Provider.values.find(_.toString == str).orNull,
      _.toString
    )
  implicit val roleMeta: Meta[Role] =
    Meta[String].xmap[Role](
      str => Role.values.find(_.toString == str).orNull,
      _.toString
    )
}
