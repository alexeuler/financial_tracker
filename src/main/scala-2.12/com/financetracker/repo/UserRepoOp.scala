package com.financetracker.repo

import doobie.imports._
import shapeless.{HList, :: => :::}
import com.financetracker.data._

trait UserRepoOp {
  def all: Query0[User]
  def find(provider: Provider, identity: Identity): Query0[User]
  def find(id: UserId): Query0[User]
  def create(provider: Provider, identity: Identity, password: Password, role: Role): Update0
  def update(id: UserId, values: HList): Update0
  def delete(id: UserId): Update0
  def deleteAll: Update0
}

object UserRepoOp extends UserRepoOp {
  def all: Query0[User] = {
    sql"select * from users".query[User]
  }

  def find(provider: Provider, identity: Identity): Query0[User] =
    sql"select * from users where provider=$provider and identity=$identity".query[User]

  def find(id: UserId): Query0[User] =
    sql"select * from users where id=$id".query[User]

  def create(provider: Provider, identity: Identity, password: Password, role: Role): Update0 =
    sql"insert into users (provider, identity, password, role) values ($provider, $identity, $password, $role)"
      .update

  def update(id: UserId, values: HList): Update0 = (
    Fragment.const("update users set ")
    ++ fieldNames(values, true)
    ++ Fragment(" where id=?", id)
  ).update

  def delete(id: UserId): Update0 =
    sql"delete from users where id=$id".update
  
  def deleteAll: Update0 =
    sql"delete from users".update

  // Using only fields allowed for update here
  private def fieldNames(list: HList, head: Boolean): Fragment = list match {
    case (x: Password) ::: xs => Fragment(nameWithComma("password", head), x) ++ fieldNames(xs, false)
    case (x: Role) ::: xs => Fragment(nameWithComma("role", head), x) ++ fieldNames(xs, false)
    case _ => Fragment.empty
  }

  private def nameWithComma(name: String, head: Boolean) =
    if (head) s"$name=?" else s", $name=?"
}