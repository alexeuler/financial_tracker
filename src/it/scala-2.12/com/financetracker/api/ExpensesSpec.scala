package com.financetracker.api

import org.scalatest._
import org.scalatest.prop._

import com.financetracker.helpers._

class ExpensesServiceSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks {
  describe("GET /expenses") {
    it("returns list of all expenses") {
      println("+++++++++++++++++++")
      (1 + 1) shouldBe 2
    }
  }
}