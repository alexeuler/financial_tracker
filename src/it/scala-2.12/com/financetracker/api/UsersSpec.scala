package com.financetracker.api

import org.scalatest._
import org.scalatest.prop._
import org.http4s.client.blaze._

import com.financetracker.helpers._

class UsersServiceSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("GET /users") {
    it("returns list of all users") {
      async {
        httpClient.expect[String](baseUrl / "users").map {resp =>
          resp shouldBe "123"
        }
      }
    }
  }
}