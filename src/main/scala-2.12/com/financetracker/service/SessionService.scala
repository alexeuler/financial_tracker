package com.financetracker.service

import pdi.jwt.{JwtAlgorithm, JwtCirce}
import io.circe._
import io.circe.syntax._
import io.circe.generic.JsonCodec
import fs2.interop.cats._
import java.util.Date
import scala.concurrent.duration._

import com.financetracker.repo._
import com.financetracker.env._
import com.financetracker.data._
import com.financetracker.types._

trait SessionService {
  def login(identity: Identity, password: Password): TaskAttempt[JWToken]
  def getSessionData(token: JWToken): TaskAttempt[Session]
  def authorize(token: JWToken, roles: Role*): TaskAttempt[Unit]
}

class SessionServiceImpl(expiration: Duration, key: String, userRepo: UserRepo) extends SessionService {

  override def login(id: Identity, password: Password): TaskAttempt[JWToken] =
    for {
      maybeUser <- userRepo.find(Provider.Email, id)
      user <- maybeUser.fold[TaskAttempt[User]](TaskAttempt.fail(UnauthorizedServiceException))(TaskAttempt.pure(_))
      _ <- if (password == user.password) TaskAttempt.pure(()) else TaskAttempt.fail(UnauthorizedServiceException)
    } yield createJWT(user)

  override def getSessionData(token: JWToken): TaskAttempt[Session] = {
    val maybeSession = for {
      json <- JwtCirce.decodeJson(token.value, key, Seq(algo)).toOption
      sessionData <- json.as[Session].toOption
    } yield sessionData

    maybeSession.fold[TaskAttempt[Session]](TaskAttempt.fail(UnauthorizedServiceException)) {
      case session@Session(_, _, expires) if expires > (new Date()).getTime => TaskAttempt.pure(session)
      case _ => TaskAttempt.fail(OutdatedTokenServiceException)
    }
  }

  override def authorize(token: JWToken, roles: Role*): TaskAttempt[Unit] =
    for {
      session <- getSessionData(token)
      role = session.role
      _ <- if (roles.exists(_ == role)) TaskAttempt.pure(()) else TaskAttempt.fail(UnauthorizedServiceException)
    } yield ()

  private val algo = JwtAlgorithm.HS256

  private def createJWT(user: User): JWToken = {
    val now = new Date().getTime
    val exp = new Date(now + expiration.toMillis).getTime

    val claim: Json = Session(user.id, user.role, exp).asJson

    val token = JwtCirce.encode(claim, key, algo)
    JWToken(token)
  }
}