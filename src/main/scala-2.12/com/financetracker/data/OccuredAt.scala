package com.financetracker.data

import doobie.imports.Meta
import io.circe._
import java.sql.Timestamp

case class OccuredAt(value: Timestamp) extends AnyVal

object OccuredAt {
  implicit val encoder: Encoder[OccuredAt] = encodeTimestamp.contramap[OccuredAt](_.value)
  implicit val decoder: Decoder[OccuredAt] = decodeTimestamp.map(OccuredAt(_))

  implicit val AmountMeta: Meta[OccuredAt] =
    Meta[Timestamp].xmap[OccuredAt](x => OccuredAt(x), _.value)   
}