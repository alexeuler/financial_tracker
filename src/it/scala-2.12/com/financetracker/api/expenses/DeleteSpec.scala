package com.financetracker.api.expenses

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
  describe("DELETE /users/:id/expenses") {
    describe("admin") {
      it("deletes expense for any user") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : true
          }      
        """

        val payloadCreate = """
          {
            "amount": 100,
            "description": "Desc",
            "comment": "Comment",
            "occuredAt": "2017-01-05 20:52"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.Admin)
            user = userWithTokenAndUsers._3.find(_.role == Role.User).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payloadCreate).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            responseCreate <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            id = responseCreate.hcursor.downField("result").downField("id").as[Int].right.get
            req1 = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / id.toString,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req1))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

          }
        }
      }
    }

    describe("manager") {
      it("fails with 300 for trying to delete any user's expense") {
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
            userWithTokenAndUsers <- loggedInUser(Role.Manager)
            user = userWithTokenAndUsers._3.find(_.role == Role.User).get
            expenses <- env.expenseRepo.all(user.id)
            req1 = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / expenses.head.id.value.toString,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req1))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

          }
        }
      }
    }

    describe("user") {

      it("deletes expense for himself") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : true
          }      
        """

        val payloadCreate = """
          {
            "amount": 100,
            "description": "Desc",
            "comment": "Comment",
            "occuredAt": "2017-01-05 20:52"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._3.find(_.role == Role.User).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payloadCreate).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            responseCreate <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            id = responseCreate.hcursor.downField("result").downField("id").as[Int].right.get
            req1 = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / id.toString,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req1))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

          }
        }
      }


      it("fails with 300 for trying to delete any user's expense") {
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
            user = userWithTokenAndUsers._3.find(_.role == Role.Manager).get
            expenses <- env.expenseRepo.all(user.id)
            req1 = Request(
              method = DELETE, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / expenses.head.id.value.toString,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req1))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

          }
        }
      }
    }

    describe("anonymous") {
      it("fails with 300 for trying to delete any user's expense") {
        val expected: Json = json"""
          {
            "error" : {
              "code": 300,
              "message": "Unauthorized"
            },
            "result" : null
          }      
        """

        val payloadUpdate = """
          {
            "amount": 200,
            "description": "Desc1",
            "comment": "",
            "occuredAt": "2017-01-07 10:52"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._3.find(_.role == Role.User).get
            expenses <- env.expenseRepo.all(user.id)
            bodyUpdate <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payloadUpdate).map(_.body))
            req1 = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / expenses.head.id.value.toString,
              body = bodyUpdate,
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req1))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

          }
        }
      }

    }
  }
}