package com.financetracker.data

import java.sql.Timestamp

case class User(
  id: UserId,
  provider: Provider,
  identity: Identity,
  password: Password,
  role: Role,
  createdAt: Timestamp,
  updatedAt: Timestamp
)