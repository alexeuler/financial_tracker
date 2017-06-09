organization := "com.financetracker"
scalaVersion := "2.12.2"
version := "0.1.4"
name := "Financial tracker"

val http4sVersion = "0.17.0-M2"
val doobieVersion = "0.4.1"
val circeVersion = "0.7.1"

libraryDependencies ++= Seq(
  // app config
  "com.typesafe" % "config" % "1.3.1",

  // http4s (http server) dsl + core
  "org.http4s" %% "http4s-dsl" % http4sVersion,

  // web server for http4s
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,

  // circe (json) integration
  "org.http4s" %% "http4s-circe" % http4sVersion,

  // generic type to json derivation
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,

  // json string interpolation
  "io.circe" %% "circe-literal" % circeVersion,

  // json web token encoding and decoding
  "com.pauldijou" %% "jwt-circe" % "0.12.1",

  // Doobie (SQL ORM)
  "org.tpolecat" %% "doobie-core-cats" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres-cats" % doobieVersion,

  // Doobie connection pool manager
  "org.tpolecat" %% "doobie-hikari-cats" % doobieVersion,

  // Shapeless (generic type programming)
  "com.chuusai" %% "shapeless" % "2.3.2",

  // Logging
  "ch.qos.logback" % "logback-classic" % "1.1.11",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.5" % Test,
  "org.tpolecat" %% "doobie-scalatest-cats" % doobieVersion % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
)

// Get current environment
val env = sys.props.get("env").orElse(sys.env.get("BUILD_ENV")) match {
  case Some("prod") | Some("production") => "prod"
  case _ => "dev"
}

// Add .conf files to resources
unmanagedResourceDirectories in Compile ++= Seq(
  baseDirectory.value / "conf" / "base",
  baseDirectory.value / "conf" / env,
  baseDirectory.value / "public"
)

// Add .conf files to resources (specific for tests)
unmanagedResourceDirectories in Test ++= Seq(
  baseDirectory.value / "conf" / "base",
  baseDirectory.value / "conf" / "secrets",
  baseDirectory.value / "conf" / "test",
  baseDirectory.value / "public"
)

// Fork JVM when running tests
fork in Test := true
javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled")

// Display all compile warnings
scalacOptions ++= List("-deprecation", "-feature")

// Sbt native packager
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
dockerExposedPorts := Seq(9000)
dockerExposedVolumes := Seq("/opt/docker/logs")
