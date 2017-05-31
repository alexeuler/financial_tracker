package com.financetracker.data

import java.sql.Timestamp
import io.circe._

case class User(
  id: UserId,
  provider: Provider,
  identity: Identity,
  password: Password,
  role: Role,
  createdAt: Timestamp,
  updatedAt: Timestamp
)

object User {
  import io.circe.generic.semiauto._
  implicit val encoder: Encoder[User] = deriveEncoder[User]
}