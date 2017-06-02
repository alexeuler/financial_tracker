package com.financetracker.data

import doobie.imports.Meta
import io.circe._

case class Comment(value: String) extends AnyVal

object Comment {
  implicit val encoder: Encoder[Comment] = Encoder.encodeString.contramap[Comment](_.value)
  implicit val decoder: Decoder[Comment] = Decoder.decodeString.map(Comment(_))

  implicit val CommentMeta: Meta[Comment] =
    Meta[String].xmap[Comment](x => Comment(x), _.value)
    
}