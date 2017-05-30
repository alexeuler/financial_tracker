package com.financetracker.service

import org.scalatest._
import org.scalatest.prop._
import org.scalamock.scalatest.MockFactory
import doobie.scalatest.imports._
import doobie.imports._
import cats._
import cats.implicits._
import fs2.interop.cats._
import java.sql.Timestamp

import shapeless.{::, HNil}

import scala.concurrent._
import scala.concurrent.duration._
import com.financetracker.repo._
import com.financetracker.helpers._
import com.financetracker.data._
import com.financetracker.types._


class UserServiceSpec extends FunSpec with Matchers with BeforeAndAfter with AsyncPropertyChecks with PropertyChecks with MockFactory {

  val mockToken = JWToken("token")
  val mockUser = User(
    UserId(1),
    Provider.Email,
    Identity("123@gmail.com"),
    Password("123"),
    Role.Unconfirmed,
    new Timestamp(0),
    new Timestamp(0)
  )
  val mockJWTService = stub[JWTService]
  (mockJWTService.createJWT _).when(mockUser).returns(mockToken)

  describe("login") {
    describe("user exists and passwords match") {
      it("returns some token") {
        async {
          val mockUserRepo = stub[UserRepo]
          (mockUserRepo.find _).when(mockUser.provider, mockUser.identity).returns(TaskAttempt.pure(Option(mockUser)))
          val userService = UserServiceImpl(mockUserRepo, mockJWTService)

          userService
            .login(mockUser.provider, mockUser.identity, mockUser.password)
            .map(value => value shouldBe Some(mockToken))
            .value
        }
      }
    }

    describe("user doesn't exist") {
      it("returns None") {
        async {
          val mockUserRepo = stub[UserRepo]
          (mockUserRepo.find _).when(mockUser.provider, mockUser.identity).returns(TaskAttempt.pure(None))
          val userService = UserServiceImpl(mockUserRepo, mockJWTService)

          userService
            .login(mockUser.provider, mockUser.identity, mockUser.password)
            .map(value => value shouldBe None)
        }
      }
    }

    describe("user exists, but password doesn't match'") {
      it("returns None") {
        async {
          val mockUserRepo = stub[UserRepo]
          (mockUserRepo.find _).when(mockUser.provider, mockUser.identity).returns(TaskAttempt.pure(Option(mockUser)))
          val userService = UserServiceImpl(mockUserRepo, mockJWTService)

          userService
            .login(mockUser.provider, mockUser.identity, Password("Invalid password"))
            .map(value => value shouldBe None)
        }
      }
    }

  }
}