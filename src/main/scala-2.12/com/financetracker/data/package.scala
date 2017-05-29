package com.financetracker

import java.sql.Timestamp

import doobie.imports.Meta
import io.circe.Encoder


package object data {
  type Provider = Provider.Value
  type Role = Role.Value

  implicit val encodeProvider: Encoder[Provider] =
    Encoder.encodeString.contramap[Provider](_.toString)
  implicit val encodeRole: Encoder[Role] =
    Encoder.encodeString.contramap[Role](_.toString)

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

  implicit val encodeTimestamp: Encoder[Timestamp] =
    Encoder.encodeString.contramap[Timestamp](_.toString)
}
