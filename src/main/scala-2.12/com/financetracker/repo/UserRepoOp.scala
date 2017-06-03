package com.financetracker.repo

import doobie.imports._
import shapeless.{HList, :: => :::, HNil}
import com.financetracker.data._
import java.sql.Timestamp
import java.util.Date

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

  def update(id: UserId, values: HList): Update0 =
    (
      fr"update users set"
      ++ fieldNames(values)
      ++ fr"updated_at=${new Timestamp((new Date()).getTime)}"
      ++ fr"where id=${id}"
    ).update

  def delete(id: UserId): Update0 =
    sql"delete from users where id=$id".update
  
  def deleteAll: Update0 =
    sql"delete from users".update

  // Using only fields allowed for update here
  private def fieldNames(list: HList): Fragment = list match {
    case (x: Password) ::: xs => 
      fr"password=${x}," ++ fieldNames(xs)
    case (x: Role) ::: xs => 
      fr"role=${x}," ++ fieldNames(xs)
    case HNil => Fragment.empty
    case _ ::: xs => Fragment.empty ++ fieldNames(xs)
  }

  private def maybeComma(tail: HList): Fragment = tail match {
    case HNil => Fragment.empty
    case _ => fr","
  }
}