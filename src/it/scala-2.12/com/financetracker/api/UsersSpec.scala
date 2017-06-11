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

class UsersServiceSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("GET /users") {
    it("returns list of all users") {
      val expected: Json = json"""
        {
          "error" : null,
          "result" : [
            {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "admin@gmail.com",
              "password" : "[filtered]",
              "role" : "Admin",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            },
            {
              "id" : 506,
              "provider" : "Email",
              "identity" : "manager@gmail.com",
              "password" : "[filtered]",
              "role" : "Manager",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            },
            {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "user@gmail.com",
              "password" : "[filtered]",
              "role" : "User",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          ]
        }      
      """

      async {
        for {
          userWithToken <- loggedInUser(Role.Admin)
          req = Request(
            method = GET, 
            uri = baseUrl / "users", 
            headers = Headers(Header("authorization", s"Bearer ${userWithToken._2.value}"))
          )
          response <- TaskAttempt.liftT(httpClient.expect[Json](req))
        } yield {
          withClue(s"Response: $response, expected: $expected: ") {
            compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
          }
        }
      }


      // async {
      //   httpClient.expect[String](baseUrl / "users").map {resp =>
      //     resp shouldBe "123"
      //   }
      // }
    }
  }
}