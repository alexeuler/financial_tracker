package com.financetracker.service

import org.http4s._
import org.http4s.headers.Authorization
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
  def getSessionData(req: Request): TaskAttempt[Session]
}

class SessionServiceImpl(expiration: Duration, key: String, userRepo: UserRepo) extends SessionService {

  def login(id: Identity, password: Password): TaskAttempt[JWToken] =
    for {
      maybeUser <- userRepo.find(Provider.Email, id)
      user <- maybeUser.fold[TaskAttempt[User]](TaskAttempt.fail(UnauthorizedServiceException))(TaskAttempt.pure(_))
      _ <- if (password == user.password) TaskAttempt.pure(()) else TaskAttempt.fail(UnauthorizedServiceException)
    } yield createJWT(user)

  def getSessionData(req: Request): TaskAttempt[Session] =
    for {
      token <- getAuthToken(req)
      session <- getSessionDataFromToken(token)
    } yield session

  private def getAuthToken(request: Request): TaskAttempt[JWToken] = {
    val maybeToken = for {
      authHeader <- request.headers.get(Authorization)
      token <- authHeader.value.split(" ") match {
        case Array("Bearer", token) => Some(JWToken(token))
        case _ => None
      }
    } yield token

    maybeToken.fold[TaskAttempt[JWToken]](TaskAttempt.fail(UnauthorizedServiceException))(TaskAttempt.pure(_))
  }

  private def getSessionDataFromToken(token: JWToken): TaskAttempt[Session] = {
    val maybeSession = for {
      json <- JwtCirce.decodeJson(token.value, key, Seq(algo)).toOption
      sessionData <- json.as[Session].toOption
    } yield sessionData

    maybeSession.fold[TaskAttempt[Session]](TaskAttempt.fail(UnauthorizedServiceException)) {
      case session@Session(_, _, _, expires) if expires > (new Date()).getTime => TaskAttempt.pure(session)
      case _ => TaskAttempt.fail(OutdatedTokenServiceException)
    }
  }

  private val algo = JwtAlgorithm.HS256

  private def createJWT(user: User): JWToken = {
    val now = new Date().getTime
    val exp = new Date(now + expiration.toMillis).getTime

    val claim: Json = Session(user.id, user.identity, user.role, exp).asJson

    val token = JwtCirce.encode(claim, key, algo)
    JWToken(token)
  }
}