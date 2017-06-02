package com.financetracker.repo

import org.scalatest._
import org.scalatest.prop._
import doobie.scalatest.imports._
import doobie.imports._
import cats._, cats.data._, cats.implicits._
import fs2.interop.cats._
import shapeless.{::, HNil}

import com.financetracker.data._
import com.financetracker.helpers._


class UserRepoOpSpec extends FunSpec with Matchers with BeforeAndAfter with PropertyChecks with QueryChecker {

  val transactor = lightTransactor

  val dbWithUsers: ConnectionIO[Unit] = for {
        _ <- ExpenseRepoOp.deleteAll.run
        _ <- UserRepoOp.deleteAll.run
        _ <- UserRepoOp.create(Provider.Email, Identity("1@gmail.com"), Password("password1"), Role.User).run
        _ <- UserRepoOp.create(Provider.Email, Identity("2@gmail.com"), Password("password2"), Role.User).run
  } yield (())

  describe("all") {
    it("typechecks") { check(UserRepoOp.all) }

    it("returns all users") {
      val query = for {
        _ <- dbWithUsers
        users <- UserRepoOp.all.list
      } yield users

      val res = query.transact(transactor).unsafePerformIO

      res.map(_.identity.value) shouldBe List("1@gmail.com", "2@gmail.com")
    }
  }

  describe("find by id") {
    it("typechecks") { check(UserRepoOp.find(UserId(1))) }

    it("finds by id") {
      val query = for {
        _ <- dbWithUsers
        userWithId <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
        user <- UserRepoOp.find(userWithId.id).option
      } yield user

      val res = query.transact(transactor).unsafePerformIO

      res.map(_.identity.value) shouldBe Some("2@gmail.com")
    }
  }

  describe("find by identity") {
    it("typechecks") { check(UserRepoOp.find(Provider.Email, Identity("123@gmail.com"))) }

    it("finds by identity") {
      val query = for {
        _ <- dbWithUsers
        user <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).option
      } yield user

      val res = query.transact(transactor).unsafePerformIO

      res.map(_.identity.value) shouldBe Some("2@gmail.com")
    }
  }
  
  describe("create") {
    it("typechecks") { check(UserRepoOp.create(Provider.Email, Identity("123@gmail.com"), Password("password"), Role.User)) }

    it("creates a user") { 
      // do nothing here since in essence this was tested by cases above 
    }
  }

  describe("update") {
    it("typechecks") { check(UserRepoOp.update(UserId(1), Password("new_pass") :: Role.User :: HNil )) }

    it("updated fields for user") {
      val query = for {
        _ <- dbWithUsers
        userBefore <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
        _ <- UserRepoOp.update(userBefore.id, Role.Manager :: Password("new_pass") :: HNil).run
        userAfter <- UserRepoOp.find(Provider.Email, Identity("2@gmail.com")).unique
      } yield (userBefore, userAfter)

      val (before, after) = query.transact(transactor).unsafePerformIO

      after.role shouldBe Role.Manager
      after.password shouldBe Password("new_pass")
      after.password should not equal (before.password)
      after.role should not equal (before.role)
      after.updatedAt should not equal (before.updatedAt)
    }
  }

  describe("delete") {
    it("typechecks") { check(UserRepoOp.delete(UserId(1))) }

    it("deletes a user") {
      val query = for {
        _ <- dbWithUsers
        userWithId <- UserRepoOp.find(Provider.Email, Identity("1@gmail.com")).unique
        _ <- UserRepoOp.delete(userWithId.id).run
        users <- UserRepoOp.all.list
      } yield (users)

      val res = query.transact(transactor).unsafePerformIO

      res.map(_.identity.value) shouldBe List("2@gmail.com")
    }
  }

  describe("deleteAll") {
    it("typechecks") { check(UserRepoOp.deleteAll) }

    it("deletes a user") {
      val query = for {
        _ <- dbWithUsers
        _ <- UserRepoOp.deleteAll.run
        users <- UserRepoOp.all.list
      } yield (users)

      val res = query.transact(transactor).unsafePerformIO

      res shouldBe List()
    }
  }
}