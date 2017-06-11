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

class UpdateSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("PATCH /users") {
    describe("admin") {
      it("updates any user's fields: role and password") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "user@gmail.com",
              "password" : "[filtered]",
              "role" : "Manager",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
          {
            "role": "Manager",
            "password": "new_pass"
          }
        """

        async {
          for {
            adminWithTokenAndUsers <- loggedInUser(Role.Admin)
            user = adminWithTokenAndUsers._3.find(_.role == Role.User).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString,
              body = body,
              headers = authHeader(adminWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            token <- env.sessionService.login(Identity("user@gmail.com"), Password("new_pass"))
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

      it("cannot update: email or identity") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "user@gmail.com",
              "password" : "[filtered]",
              "role" : "User",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
          {
            "identity": "new@email.com",
            "provider": "Facebook"
          }
        """

        async {
          for {
            adminWithTokenAndUsers <- loggedInUser(Role.Admin)
            user = adminWithTokenAndUsers._3.find(_.role == Role.User).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString,
              body = body,
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

      describe("User with id doesn't exist") {
        it("fails with 304 entity not found") {
          // Entity not found
          val expected: Json = json"""
            {
              "error" : {
                "code": 304,
                "message": "Entity not found"
              },
              "result" : null
            }      
          """

          val payload = """
            {
              "identity": "new@email.com",
              "provider": "Facebook"
            }
          """

          async {
            for {
              adminWithTokenAndUsers <- loggedInUser(Role.Admin)
              body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
              req = Request(
                method = PATCH, 
                uri = baseUrl / "users" / "0",
                body = body,
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



    }

    describe("manager") {
      it("updates any user's fields: role and password") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "user@gmail.com",
              "password" : "[filtered]",
              "role" : "Manager",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
          {
            "role": "Manager",
            "password": "new_pass"
          }
        """

        async {
          for {
            managerWithTokenAndUsers <- loggedInUser(Role.Manager)
            user = managerWithTokenAndUsers._3.find(_.role == Role.User).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString,
              body = body,
              headers = authHeader(managerWithTokenAndUsers._2)
            )
            response <- TaskAttempt.liftT(httpClient.fetchAs[Json](req))
            token <- env.sessionService.login(Identity("user@gmail.com"), Password("new_pass"))
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

      it("cannot update: email or identity") {
        val expected: Json = json"""
          {
            "error" : null,
            "result" : {
              "id" : "ignored",
              "provider" : "Email",
              "identity" : "user@gmail.com",
              "password" : "[filtered]",
              "role" : "User",
              "createdAt" : "ignored",
              "updatedAt" : "ignored"
            }
          }      
        """

        val payload = """
          {
            "identity": "new@email.com",
            "provider": "Facebook"
          }
        """

        async {
          for {
            managerWithTokenAndUsers <- loggedInUser(Role.Manager)
            user = managerWithTokenAndUsers._3.find(_.role == Role.User).get
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString,
              body = body,
              headers = authHeader(managerWithTokenAndUsers._2)
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
      it("fails with 300 unauthorized if tries to update any user's fields") {
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
            "role": "Manager",
            "password": "new_pass"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._1
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString,
              body = body,
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
      it("fails with 300 unauthorized if tries to update any user's fields") {
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
            "role": "Manager",
            "password": "new_pass"
          }
        """

        async {
          for {
            userWithTokenAndUsers <- loggedInUser(Role.User)
            user = userWithTokenAndUsers._1
            body <- TaskAttempt.liftT(EntityEncoder[String].toEntity(payload).map(_.body))
            req = Request(
              method = PATCH, 
              uri = baseUrl / "users" / user.id.value.toString,
              body = body,
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