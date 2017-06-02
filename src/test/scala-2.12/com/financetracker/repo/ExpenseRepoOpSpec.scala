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

    it("returns all users") {
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

      val resForUser1 = queryForUser1.transact(transactor).unsafePerformIO
      val resForUser2 = queryForUser2.transact(transactor).unsafePerformIO

      resForUser1.map(_.amount.value) shouldBe List(100, 50)
      resForUser2.map(_.amount.value) shouldBe List(20)
    }
  }

  // describe("find by id") {
  //   it("typechecks") { check(ExpenseRepoOp.find(UserId(1))) }

  //   it("finds by id") {
  //     val query = for {
  //       _ <- dbWithUsers
  //       userWithId <- ExpenseRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
  //       user <- ExpenseRepoOp.find(userWithId.id).option
  //     } yield user

  //     val res = query.transact(transactor).unsafePerformIO

  //     res.map(_.identity.value) shouldBe Some("2@gmail.com")
  //   }
  // }

  // describe("find by identity") {
  //   it("typechecks") { check(ExpenseRepoOp.find(Provider.Email, Identity("123@gmail.com"))) }

  //   it("finds by identity") {
  //     val query = for {
  //       _ <- dbWithUsers
  //       user <- ExpenseRepoOp.find(Provider.Email, Identity("2@gmail.com")).option
  //     } yield user

  //     val res = query.transact(transactor).unsafePerformIO

  //     res.map(_.identity.value) shouldBe Some("2@gmail.com")
  //   }
  // }
  
  // describe("create") {
  //   it("typechecks") { check(ExpenseRepoOp.create(Provider.Email, Identity("123@gmail.com"), Password("password"), Role.Unconfirmed)) }

  //   it("creates a user") { 
  //     // do nothing here since in essence this was tested by cases above 
  //   }
  // }

  // describe("update") {
  //   it("typechecks") { check(ExpenseRepoOp.update(UserId(1), Password("new_pass") :: Role.Unconfirmed :: HNil )) }

  //   it("updated fields for user") {
  //     val query = for {
  //       _ <- dbWithUsers
  //       userBefore <- ExpenseRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
  //       _ <- ExpenseRepoOp.update(userBefore.id, Role.User :: Password("new_pass") :: HNil).run
  //       userAfter <- ExpenseRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
  //     } yield (userBefore, userAfter)

  //     val (before, after) = query.transact(transactor).unsafePerformIO

  //     after.role shouldBe Role.User
  //     after.password shouldBe Password("new_pass")
  //     after.password should not equal (before.password)
  //     after.role should not equal (before.role)
  //     after.updatedAt should not equal (before.updatedAt)
  //   }
  // }

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