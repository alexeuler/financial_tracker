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

class ListSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("GET /users") {
    describe("admin") {
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
              headers = authHeader(userWithToken._2)
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


    describe("manager") {
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
            userWithToken <- loggedInUser(Role.Manager)
            req = Request(
              method = GET, 
              uri = baseUrl / "users", 
              headers = authHeader(userWithToken._2)
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

    describe("user") {
      it("fails with 300 unauthorized") {
        val expected: Json = json"""
          {
            "error" : {
              "code": 300,
              "message": "Unauthorized"
            },
            "result" : null
          }      
        """

        async {
          for {
            userWithToken <- loggedInUser(Role.User)
            req = Request(
              method = GET, 
              uri = baseUrl / "users", 
              headers = authHeader(userWithToken._2)
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

    describe("not logged in") {
      it("fails with 300 unauthorized") {
        val expected: Json = json"""
          {
            "error" : {
              "code": 300,
              "message": "Unauthorized"
            },
            "result" : null
          }      
        """

        async {
          val req = Request(
            method = GET, 
            uri = baseUrl / "users"
          )
          for {
            _ <- serviceWithUsers
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