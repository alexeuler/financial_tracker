organization := "com.financetracker"
scalaVersion := "2.12.2"
version := "0.1.0"
name := "Financial tracker"

val http4sVersion = "0.17.+"
val doobieVersion = "0.4.+"

libraryDependencies ++= Seq(
  // app config
  "com.typesafe" % "config" % "1.3.+",

  // http4s (http server) dsl + core
  "org.http4s" %% "http4s-dsl" % http4sVersion,

  // web server for http4s
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,

  // circe (json) integration
  "org.http4s" %% "http4s-circe" % http4sVersion,

  // generic type to json derivation
  "io.circe" %% "circe-generic" % "0.7.+",

  // json string interpolation
  "io.circe" %% "circe-literal" % "0.7.+",

  // Doobie (SQL ORM)
  "org.tpolecat" %% "doobie-core-cats" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres-cats" % doobieVersion,

  // Doobie connection pool manager
  "org.tpolecat" %% "doobie-hikari-cats" % doobieVersion,

  // Shapeless (generic type programming)
  "com.chuusai" %% "shapeless" % "2.3.+",

  // Logging
  "ch.qos.logback" % "logback-classic" % "1.1.+",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.+"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.+" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.+" % Test,
  "org.tpolecat" %% "doobie-scalatest-cats" % doobieVersion % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.+" % Test
)

// Add .conf files to resources
unmanagedResourceDirectories in Compile ++= Seq(
  baseDirectory.value / "conf" / "base",
  baseDirectory.value / "conf" / "secrets",
  baseDirectory.value / "conf" / "dev"
)

// Add .conf files to resources (specific for tests)
unmanagedResourceDirectories in Test ++= Seq(
  baseDirectory.value / "conf" / "base",
  baseDirectory.value / "conf" / "secrets",
  baseDirectory.value / "conf" / "test"
)

// Fork JVM when running tests
fork in Test := true
javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")

// Display all compile warnings
scalacOptions ++= List("-deprecation", "-feature")