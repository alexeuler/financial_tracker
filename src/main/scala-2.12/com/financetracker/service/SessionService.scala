package com.financetracker.service

import pdi.jwt.{JwtAlgorithm, JwtCirce}
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import fs2.interop.cats._
import java.util.Date

import scala.concurrent.duration._
import com.financetracker.repo._
import com.financetracker.env._
import com.financetracker.data._
import com.financetracker.types._

trait SessionService {
  def login(identity: Identity, password: Password): TaskAttempt[JWToken]
}

class SessionServiceImpl(expiration: Duration, key: String, userRepo: UserRepo) extends SessionService {

  override def login(id: Identity, password: Password): TaskAttempt[JWToken] =
    for {
      maybeUser <- userRepo.find(Provider.Email, id)
      user <- maybeUser.fold[TaskAttempt[User]](TaskAttempt.fail(UnauthorizedServiceException))(TaskAttempt.pure(_))
      _ <- if (password == user.password) TaskAttempt.pure(()) else TaskAttempt.fail(UnauthorizedServiceException)
    } yield createJWT(user)

  private def createJWT(user: User): JWToken = {
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