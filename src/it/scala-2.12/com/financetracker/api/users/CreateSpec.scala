package com.financetracker.api.users

import org.scalatest._
import org.scalatest.prop._
import org.http4s._
import org.http4s.dsl._
import org.http4s.circe._
import org.http4s.headers._
import org.http4s.client.blaze._
import fs2.interop.cats._
import io.circe._
import io.circe.literal._

import com.financetracker.helpers._
import com.financetracker.data._
import com.financetracker.types._

class CreateSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("POST /users") {
    describe("any role and not logged in") {
      it("creates a user") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "newuser@gmail.com",
              "password" : "[filtered]",
              "role" : "User",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
          {
            "email": "newuser@gmail.com",
            "password": "secret"
          }
        """

        async {
          for {
            _ <- serviceWithUsers
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users",
              body = body
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            token <- env.sessionService.login(Identity("newuser@gmail.com"), Password("secret"))
            fetchReq = Request(
              method = GET,
              uri = baseUrl / "users" / response.hcursor.downField("result").downField("id").as[Int].right.get.toString(),
              headers = authHeader(token)
            )
            fetchResponse <- TaskAttempt.liftT(httpClient.fetchAs[Json](fetchReq))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
            }

            withClue(s"Fetch response: $fetchResponse, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(fetchResponse, expected) shouldBe true
            }

          }
        }
      }

      describe("Invalid email") {
        it("fails with 303 invalid email") {
          val expected = json"""
            {
              "error" : {
                "code": 303,
                "message": "Invalid email"
              },
              "result" : null
            }      
          """

          val payload = """
            {
              "email": "invalid_email",
              "password": "secret"
            }
          """

          async {
            for {
              _ <- serviceWithUsers
              body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
              req = Request(
                method = POST,
                uri = baseUrl / "users",
                body = body
              )
              response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            } yield {
              withClue(s"Response: $response, expected: $expected: ") {
                compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
              }
            }
          }
        }
      }

      describe("User with this email already exists") {
        it("fails with 302 user alredy exists") {
          val expected = json"""
            {
              "error" : {
                "code": 302,
                "message": "User with this email already exists"
              },
              "result" : null
            }      
          """

          val payload = """
            {
              "email": "admin@gmail.com",
              "password": "secret"
            }
          """

          async {
            for {
              _ <- serviceWithUsers
              body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
              req = Request(
                method = POST,
                uri = baseUrl / "users",
                body = body
              )
              response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            } yield {
              withClue(s"Response: $response, expected: $expected: ") {
                compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
              }
            }
          }
        }
      }


      describe("Malformed Json") {
        it("fails with 200 malformed json") {
          val expected = json"""
            {
              "error" : {
                "code": 200,
                "message": "Malformed json body in request"
              },
              "result" : null
            }      
          """

          val payload = """
              "email": "newuser@gmail.com",
              "password": "secret"
            }
          """

          async {
            for {
              _ <- serviceWithUsers
              body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
              req = Request(
                method = POST,
                uri = baseUrl / "users",
                body = body
              )
              response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            } yield {
              withClue(s"Response: $response, expected: $expected: ") {
                compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
              }
            }
          }
        }
      }

      describe("Json with missing fields") {
        it("fails with 201 json type doesn't match") {
          val expected = json"""
            {
              "error" : {
                "code": 201,
                "message": "Json structure doesn't match expected type"
              },
              "result" : null
            }
          """

          val payload = """
            {
              "email": "newuser@gmail.com"
            }
          """

          async {
            for {
              _ <- serviceWithUsers
              body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
              req = Request(
                method = POST,
                uri = baseUrl / "users",
                body = body
              )
              response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            } yield {
              withClue(s"Response: $response, expected: $expected: ") {
                compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
              }
            }
          }
        }
      }

      describe("Json with extra fields") {
        it("creates a user") {
          val expected: Json = json"""
            {
              "error" : null,
              "result" : {
                "id" : "ignored",
                "provider" : "Email",
                "identity" : "newuser@gmail.com",
                "password" : "[filtered]",
                "role" : "User",
                "createdAt" : "ignored",
                "updatedAt" : "ignored"
              }
            }      
          """

          val payload = """
            {
              "email": "newuser@gmail.com",
              "password": "secret",
              "someOtherField": "value"
            }
          """

          async {
            for {
              _ <- serviceWithUsers
              body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
              req = Request(
                method = POST, 
                uri = baseUrl / "users",
                body = body
              )
              response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            } yield {
              withClue(s"Response: $response, expected: $expected: ") {
                compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
              }
            }
          }
        }
      }


    }
  }
}