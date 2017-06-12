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

class CreateSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("POST /users/:id/expenses") {
    describe("admin") {
      it("creates expense for any user") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "amount": 100,
              "description": "Desc",
              "comment": "Comment",
              "occuredAt": "2017-01-05 20:52",
              "userId": "ignored",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
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
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            fetchReq = Request(
              method = GET,
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / response.hcursor.downField("result").downField("id").as[Int].right.get.toString(),
              headers = authHeader(userWithTokenAndUsers._2)
            )
            fetchResponse <- TaskAttempt.liftT(httpClient.fetchAs[Json](fetchReq))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

            withClue(s"Fetch response: $fetchResponse, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(fetchResponse, expected) shouldBe true
            }

          }
        }
      }
    }

    describe("manager") {
      it("creates expense for himself") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "amount": 100,
              "description": "Desc",
              "comment": "Comment",
              "occuredAt": "2017-01-05 20:52",
              "userId": "ignored",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
          {
            "amount": 100,
            "description": "Desc",
            "comment": "Comment",
            "occuredAt": "2017-01-05 20:52"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.Manager)
            user = userWithTokenAndUsers._3.find(_.role == Role.Manager).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            fetchReq = Request(
              method = GET,
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / response.hcursor.downField("result").downField("id").as[Int].right.get.toString(),
              headers = authHeader(userWithTokenAndUsers._2)
            )
            fetchResponse <- TaskAttempt.liftT(httpClient.fetchAs[Json](fetchReq))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

            withClue(s"Fetch response: $fetchResponse, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(fetchResponse, expected) shouldBe true
            }

          }
        }
      }
    }

    describe("user") {
      it("creates expense for himself") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "amount": 100,
              "description": "Desc",
              "comment": "Comment",
              "occuredAt": "2017-01-05 20:52",
              "userId": "ignored",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
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
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            fetchReq = Request(
              method = GET,
              uri = baseUrl / "users" / user.id.value.toString / "expenses" / response.hcursor.downField("result").downField("id").as[Int].right.get.toString(),
              headers = authHeader(userWithTokenAndUsers._2)
            )
            fetchResponse <- TaskAttempt.liftT(httpClient.fetchAs[Json](fetchReq))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }

            withClue(s"Fetch response: $fetchResponse, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(fetchResponse, expected) shouldBe true
            }

          }
        }
      }

      it("fails with 300 for any other user") {
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
            "amount": 100,
            "description": "Desc",
            "comment": "Comment",
            "occuredAt": "2017-01-05 20:52"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._3.find(_.role == Role.Manager).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
              headers = authHeader(userWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }
          }
        }
      }

    }


    describe("anonymous") {
      it("fails with 300 for any user") {
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
            "amount": 100,
            "description": "Desc",
            "comment": "Comment",
            "occuredAt": "2017-01-05 20:52"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._3.find(_.role == Role.Manager).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = POST, 
              uri = baseUrl / "users" / user.id.value.toString / "expenses",
              body = body,
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
          } yield {
            withClue(s"Response: $response, expected: $expected: ") {
              compareJsonsIgnoring(List("id", "createdAt", "updatedAt", "userId"))(response, expected) shouldBe true
            }
          }
        }
      }
    }


    //   describe("Invalid email") {
    //     it("fails with 303 invalid email") {
    //       val expected = json"""
    //         {
    //           "error" : {
    //             "code": 303,
    //             "message": "Invalid email"
    //           },
    //           "result" : null
    //         }      
    //       """

    //       val payload = """
    //         {
    //           "email": "invalid_email",
    //           "password": "secret"
    //         }
    //       """

    //       async {
    //         for {
    //           _ <- serviceWithUsers
    //           body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
    //           req = Request(
    //             method = POST,
    //             uri = baseUrl / "users",
    //             body = body
    //           )
    //           response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
    //         } yield {
    //           withClue(s"Response: $response, expected: $expected: ") {
    //             compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
    //           }
    //         }
    //       }
    //     }
    //   }

    //   describe("User with this email already exists") {
    //     it("fails with 302 user alredy exists") {
    //       val expected = json"""
    //         {
    //           "error" : {
    //             "code": 302,
    //             "message": "User with this email already exists"
    //           },
    //           "result" : null
    //         }      
    //       """

    //       val payload = """
    //         {
    //           "email": "admin@gmail.com",
    //           "password": "secret"
    //         }
    //       """

    //       async {
    //         for {
    //           _ <- serviceWithUsers
    //           body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
    //           req = Request(
    //             method = POST,
    //             uri = baseUrl / "users",
    //             body = body
    //           )
    //           response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
    //         } yield {
    //           withClue(s"Response: $response, expected: $expected: ") {
    //             compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
    //           }
    //         }
    //       }
    //     }
    //   }


    //   describe("Malformed Json") {
    //     it("fails with 200 malformed json") {
    //       val expected = json"""
    //         {
    //           "error" : {
    //             "code": 200,
    //             "message": "Malformed json body in request"
    //           },
    //           "result" : null
    //         }      
    //       """

    //       val payload = """
    //           "email": "newuser@gmail.com",
    //           "password": "secret"
    //         }
    //       """

    //       async {
    //         for {
    //           _ <- serviceWithUsers
    //           body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
    //           req = Request(
    //             method = POST,
    //             uri = baseUrl / "users",
    //             body = body
    //           )
    //           response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
    //         } yield {
    //           withClue(s"Response: $response, expected: $expected: ") {
    //             compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
    //           }
    //         }
    //       }
    //     }
    //   }

    //   describe("Json with missing fields") {
    //     it("fails with 201 json type doesn't match") {
    //       val expected = json"""
    //         {
    //           "error" : {
    //             "code": 201,
    //             "message": "Json structure doesn't match expected type"
    //           },
    //           "result" : null
    //         }
    //       """

    //       val payload = """
    //         {
    //           "email": "newuser@gmail.com"
    //         }
    //       """

    //       async {
    //         for {
    //           _ <- serviceWithUsers
    //           body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
    //           req = Request(
    //             method = POST,
    //             uri = baseUrl / "users",
    //             body = body
    //           )
    //           response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
    //         } yield {
    //           withClue(s"Response: $response, expected: $expected: ") {
    //             compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
    //           }
    //         }
    //       }
    //     }
    //   }

    //   describe("Json with extra fields") {
    //     it("creates a user") {
    //       val expected: Json = json"""
    //         {
    //           "error" : null,
    //           "result" : {
    //             "id" : "ignored",
    //             "provider" : "Email",
    //             "identity" : "newuser@gmail.com",
    //             "password" : "[filtered]",
    //             "role" : "User",
    //             "createdAt" : "ignored",
    //             "updatedAt" : "ignored"
    //           }
    //         }      
    //       """

    //       val payload = """
    //         {
    //           "email": "newuser@gmail.com",
    //           "password": "secret",
    //           "someOtherField": "value"
    //         }
    //       """

    //       async {
    //         for {
    //           _ <- serviceWithUsers
    //           body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
    //           req = Request(
    //             method = POST, 
    //             uri = baseUrl / "users",
    //             body = body
    //           )
    //           response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
    //         } yield {
    //           withClue(s"Response: $response, expected: $expected: ") {
    //             compareJsonsIgnoring(List("id", "createdAt", "updatedAt"))(response, expected) shouldBe true
    //           }
    //         }
    //       }
    //     }
    //   }


    // }
  }
}