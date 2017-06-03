package com.financetracker.repo

import org.scalatest._
import org.scalatest.prop._
import doobie.scalatest.imports._
import doobie.imports._
import cats._, cats.data._, cats.implicits._
import fs2.interop.cats._
import shapeless.{::, HNil}
import java.sql.Timestamp
import java.util.Date

import com.financetracker.data._
import com.financetracker.helpers._


class ExpenseRepoOpSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks with QueryChecker {

  val transactor = lightTransactor

  val dbWithExpenses: ConnectionIO[Unit] = for {
        _ <- ExpenseRepoOp.deleteAll.run
        _ <- UserRepoOp.deleteAll.run
        _ <- UserRepoOp.create(Provider.Email, Identity("1@gmail.com"), Password("password1"), Role.User).run
        _ <- UserRepoOp.create(Provider.Email, Identity("2@gmail.com"), Password("password1"), Role.User).run
        user1 <- UserRepoOp.find(Provider.Email, Identity("1@gmail.com")).unique
        user2 <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
        _ <- ExpenseRepoOp.create(
          Amount(100),
          Description("First $100 expense"),
          Some(Comment("Woo-hoo")),
          OccuredAt(new Timestamp(0)),
          user1.id,
        ).run
        _ <- ExpenseRepoOp.create(
          Amount(50),
          Description("Second $50 expense"),
          None,
          OccuredAt(new Timestamp(new Date().getTime)),
          user1.id
        ).run
        _ <- ExpenseRepoOp.create(
          Amount(20),
          Description("First $20 expense"),
          None,
          OccuredAt(new Timestamp(new Date().getTime)),
          user2.id
        ).run
  } yield (())

  describe("all") {
    it("typechecks") { check(ExpenseRepoOp.all(UserId(1))) }

    it("returns all expenses") {
      val queryForUser1 = for {
        _ <- dbWithExpenses
        user <- UserRepoOp.find(Provider.Email, Identity("1@gmail.com")).unique
        expenses <- ExpenseRepoOp.all(user.id).list
      } yield expenses

      val queryForUser2 = for {
        _ <- dbWithExpenses
        user <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
        expenses <- ExpenseRepoOp.all(user.id).list
      } yield expenses

      val queryForUser3 = for {
        _ <- dbWithExpenses
        expenses <- ExpenseRepoOp.all(UserId(0)).list
      } yield expenses


      val resForUser1 = queryForUser1.transact(transactor).unsafePerformIO
      val resForUser2 = queryForUser2.transact(transactor).unsafePerformIO
      val resForUser3 = queryForUser3.transact(transactor).unsafePerformIO


      resForUser1.map(_.amount.value) shouldBe List(100, 50)
      resForUser2.map(_.amount.value) shouldBe List(20)
      resForUser3.map(_.amount.value) shouldBe List()
    }
  }

  describe("find") {
    it("typechecks") { check(ExpenseRepoOp.find(ExpenseId(1))) }

    it("finds expense by id") {
      val queryRes1 = for {
        _ <- dbWithExpenses
        user <- UserRepoOp.find(Provider.Email, Identity("1@gmail.com")).unique
        expenses <- ExpenseRepoOp.all(user.id).list
        expense = expenses.head
        foundExpense <- ExpenseRepoOp.find(expense.id).unique
      } yield (expense == foundExpense)

      queryRes1.transact(transactor).unsafePerformIO shouldBe true
    }
  }

  
  describe("create") {
    it("typechecks") { check(ExpenseRepoOp.create(Amount(200), Description("The $200 expense"), None, OccuredAt(new Timestamp(0)), UserId(0))) }

    it("creates an expense") { 
      // do nothing here since in essence this was tested by cases above 
    }
  }


  describe("update") {
    it("typechecks") { check(ExpenseRepoOp.update(ExpenseId(1), Amount(300) :: Description("New desc") :: HNil )) }

    it("updates fields for expense that are allowed to update and ignores other fields") {
      val currentTimestamp = new Timestamp(new Date().getTime)
      val query = for {
        _ <- dbWithExpenses
        user <- UserRepoOp.find(Provider.Email, Identity("1@gmail.com")).unique
        user2 <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
        expenses <- ExpenseRepoOp.all(user.id).list
        expenseBefore = expenses.head
        _ <- ExpenseRepoOp.update(expenseBefore.id,
          Amount(500) :: user2.id :: "Some unknown field type" ::
          Description("Some new description") :: 
          OccuredAt(currentTimestamp) :: ExpenseId(1000) ::
          Comment("Some new comment") :: HNil
        ).run
        expenseAfter <- ExpenseRepoOp.find(expenseBefore.id).unique
      } yield (expenseBefore, expenseAfter, user2)

      val (before, after, user2) = query.transact(transactor).unsafePerformIO

      after.id shouldBe before.id
      after.amount.value shouldBe 500
      after.description.value shouldBe "Some new description"
      after.comment.get.value shouldBe "Some new comment"
      after.userId shouldBe user2.id
    }
  }


  // describe("delete") {
  //   it("typechecks") { check(ExpenseRepoOp.delete(UserId(1))) }

  //   it("deletes a user") {
  //     val query = for {
  //       _ <- dbWithUsers
  //       userWithId <- ExpenseRepoOp.find(Provider.Email, Identity("1@gmail.com")).unique
  //       _ <- ExpenseRepoOp.delete(userWithId.id).run
  //       users <- ExpenseRepoOp.all.list
  //     } yield (users)

  //     val res = query.transact(transactor).unsafePerformIO

  //     res.map(_.identity.value) shouldBe List("2@gmail.com")
  //   }
  // }

  // describe("deleteAll") {
  //   it("typechecks") { check(ExpenseRepoOp.deleteAll) }

  //   it("deletes a user") {
  //     val query = for {
  //       _ <- dbWithUsers
  //       _ <- ExpenseRepoOp.deleteAll.run
  //       users <- ExpenseRepoOp.all.list
  //     } yield (users)

  //     val res = query.transact(transactor).unsafePerformIO

  //     res shouldBe List()
  //   }
  // }
}