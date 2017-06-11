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

class DeleteSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("DELETE /users/:id") {
    describe("admin") {
      it("deletes user by id") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : true
          }      
        """

        val expectedFetch: Json = json"""
            {
              "error" : {
                "code": 100,
                "message": "Not found"
              },
              "result" : null
            }      
          """

        async {
          for {
            adminWithTokenAndUsers <- loggedInUser(Role.Admin)
            user = adminWithTokenAndUsers._3.find(_.role == Role.User).get
            req = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString,
              headers = authHeader(adminWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            fetchReq = Request(
              method = GET,
              uri = baseUrl / "users" / user.id.value.toString,
              headers = authHeader(adminWithTokenAndUsers._2)
            )
            fetchResponse <- TaskAttempt.liftT(httpClient.fetchAs[Json](fetchReq))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
            }

            withClue(s"Fetch response: $fetchResponse, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(fetchResponse, expectedFetch) shouldBe true
            }

          }
        }
      }
    }

      describe("User with id doesn't exist") {
        it("returns false") {
          val expected: Json = json"""
            {
              "error" : null,
              "result" : false
            }      
          """

          async {
            for {
              adminWithTokenAndUsers <- loggedInUser(Role.Admin)
              req = Request(
                method = DELETE, 
                uri = baseUrl / "users" / "0",
                headers = authHeader(adminWithTokenAndUsers._2)
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
      it("deletes user by id") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : true
          }      
        """

        val expectedFetch: Json = json"""
            {
              "error" : {
                "code": 100,
                "message": "Not found"
              },
              "result" : null
            }      
          """

        async {
          for {
            managerWithTokenAndUsers <- loggedInUser(Role.Manager)
            user = managerWithTokenAndUsers._3.find(_.role == Role.User).get
            req = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString,
              headers = authHeader(managerWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            fetchReq = Request(
              method = GET,
              uri = baseUrl / "users" / user.id.value.toString,
              headers = authHeader(managerWithTokenAndUsers._2)
            )
            fetchResponse <- TaskAttempt.liftT(httpClient.fetchAs[Json](fetchReq))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
            }

            withClue(s"Fetch response: $fetchResponse, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(fetchResponse, expectedFetch) shouldBe true
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
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._1
            req = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString,
              headers = authHeader(userWithTokenAndUsers._2)
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

    describe("anonymous") {
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
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._1
            req = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString,
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