package com.financetracker.service

import pdi.jwt.{JwtAlgorithm, JwtCirce}
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import java.util.Date

import scala.concurrent.duration._
import com.financetracker.repo._
import com.financetracker.env._
import com.financetracker.data._

trait JWTService {
  def createJWT(user: User): JWToken
}

class JWTServiceImpl(expiration: Duration, key: String) extends JWTService {

  def createJWT(user: User): JWToken = {
    val now = new Date().getTime
    val exp = new Date(now + expiration.toMillis)

    val stringClaim: Json = Map(
      "provider" -> user.provider.toString,
      "identity" -> user.identity.value,
      "role" -> user.role.toString
    ).asJson
    val intClaim = Map("expires" -> exp.getTime).asJson

    val algo = JwtAlgorithm.HS256

    val token = JwtCirce.encode(stringClaim.deepMerge(intClaim), key, algo)
    JWToken(token)
  }
}