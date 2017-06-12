package com.financetracker.api

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

class SessionSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("POST /sessions") {
    it("logs in with correct credentials") {
      val expected: Json = json"""
        {
          "error" : null,
          "result" : "ignored"
        }      
      """

      val payload = """
        {
          "email": "admin@gmail.com",
          "password": "admin"
        }
      """

      async {
        for {
          userWithTokenAndUsers <- loggedInUser(Role.Admin)
          body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
          req = Request(
            method = POST, 
            uri = baseUrl / "sessions",
            body = body
          )
          response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
        } yield {
          withClue(s"Response: $response, expected: $expected: ") {
            compareJsonsIgnoring(List("result"))(response, expected) shouldBe true
          }

        }
      }
    }

    it("fails with 300 if credentials are incorrect") {
      val expected: Json = json"""
        {
          "error" : {
            "code": 300,
            "message": "Unauthorized"
          },
          "result" : null
        }      
      """

      val payload = """
        {
          "email": "admin@gmail.com",
          "password": "incorrect pass"
        }
      """

      async {
        for {
          userWithTokenAndUsers <- loggedInUser(Role.Admin)
          body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
          req = Request(
            method = POST, 
            uri = baseUrl / "sessions",
            body = body
          )
          response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
        } yield {
          withClue(s"Response: $response, expected: $expected: ") {
            compareJsonsIgnoring(List("result"))(response, expected) shouldBe true
          }

        }
      }
    }

  }
}