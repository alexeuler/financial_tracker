package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class ExpenseId(value: Int) extends AnyVal

object ExpenseId {
  implicit val encoder: Encoder[ExpenseId] = Encoder.encodeInt.contramap[ExpenseId](_.value)
  implicit val decoder: Decoder[ExpenseId] = Decoder.decodeInt.map(ExpenseId(_))

  implicit val idMeta: Meta[ExpenseId] =
    Meta[Int].xmap[ExpenseId](x => ExpenseId(x), _.value)
}
