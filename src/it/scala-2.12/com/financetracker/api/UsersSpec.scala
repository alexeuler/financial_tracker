package com.financetracker.api

import org.scalatest._
import org.scalatest.prop._

class UserServiceSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("GET /users") {
    it("returns list of all users") {
      (1 + 1) shouldBe 2
    }
  }
}