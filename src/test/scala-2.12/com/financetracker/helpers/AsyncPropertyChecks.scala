package com.financetracker.helpers

import fs2.Task
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.PropertyChecks

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.implicitConversions

import com.financetracker.types._
  
  trait AsyncPropertyChecks {
    propertyChecks: PropertyChecks =>

    private val timeout = 10000.millis
    private implicit def taskToFuture[A](task: Task[A]): Future[A] = task.unsafeRunAsyncFuture()

    def async[A](task: Task[A]): A = Await.result(task.unsafeRunAsyncFuture, timeout)

    def async[A](taskAttempt: TaskAttempt[A]): A = {
      val future = taskAttempt.value.map {
          case Left(e) => throw e
          case Right(x) => x
        }.unsafeRunAsyncFuture
      Await.result(future, timeout)
    }

    def forAllAsync[A, R](f: A => Task[R])(implicit arbA: Arbitrary[A]): Unit =
      forAll { a: A => Await.result(f(a), timeout) }

    def forAllAsync[A, B, R](f: (A, B) => Task[R])
                            (implicit arbA: Arbitrary[A], arbB: Arbitrary[B]): Unit =
      forAll { (a: A, b: B) => Await.result(f(a, b), timeout) }

    def forAllAsync[A, B, C, R](f: (A, B, C) => Task[R])
                               (implicit arbA: Arbitrary[A], arbB: Arbitrary[B], arbC: Arbitrary[C]): Unit =
      forAll { (a: A, b: B, c: C) => Await.result(f(a, b, c), timeout) }

    def forAllAsync[A, B, C, D, R](f: (A, B, C, D) => Task[R])
                                  (implicit arbA: Arbitrary[A], arbB: Arbitrary[B], arbC: Arbitrary[C], arbD: Arbitrary[D]): Unit =
      forAll { (a: A, b: B, c: C, d: D) => Await.result(f(a, b, c, d), timeout) }



    def forAllAsync[A, R](genA: Gen[A])
                            (f: (A) => Task[R]): Unit =
      forAll(genA) { (a: A) => Await.result(f(a), timeout) }

    def forAllAsync[A, B, R](genA: Gen[A], genB: Gen[B])
                            (f: (A, B) => Task[R]): Unit =
      forAll(genA, genB) { (a: A, b: B) => Await.result(f(a, b), timeout) }


    def forAllAsync[A, B, C, R](genA: Gen[A], genB: Gen[B], genC: Gen[C])
                               (f: (A, B, C) => Task[R]): Unit =
      forAll(genA, genB, genC) { (a: A, b: B, c: C) => Await.result(f(a, b, c), timeout) }


    def forAllAsync[A, B, C, D, R](genA: Gen[A], genB: Gen[B], genC: Gen[C], genD: Gen[D])
                                  (f: (A, B, C, D) => Task[R]): Unit =
      forAll(genA, genB, genC, genD) { (a: A, b: B, c: C, d: D) => Await.result(f(a, b, c, d), timeout) }

  }
