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
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
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
    }
  }
}