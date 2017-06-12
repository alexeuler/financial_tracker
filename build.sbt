val http4sVersion = "0.17.0-M2"
val doobieVersion = "0.4.1"
val circeVersion = "0.7.1"

lazy val commonSettings = Seq(
    organization := "com.financetracker",
    scalaVersion := "2.12.2",
    version := "0.1.9",
    name := "Financial tracker",

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
    ),

    // Display all compile warnings
    scalacOptions ++= List("-deprecation", "-feature")
)

lazy val app = (project in file("."))
  .settings(
    commonSettings,

    // Add .conf files to resources
    unmanagedResourceDirectories in Compile ++= Seq(
      baseDirectory.value / "conf" / "base",
      baseDirectory.value / "conf" / "secrets",
      baseDirectory.value / "conf" / "dev",
      baseDirectory.value / "public"
    )
  )

lazy val testPackage = project
  .in(file("build/test"))
  .enablePlugins(JavaAppPackaging)
  .configs(IntegrationTest)
  .settings(
    commonSettings,
    Defaults.itSettings,

    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.3" % "it, test",
      "org.scalacheck" %% "scalacheck" % "1.13.5" % "it, test",
      "org.tpolecat" %% "doobie-scalatest-cats" % doobieVersion % "it, test",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "it, test"
    ),

    resourceDirectory in Compile := (resourceDirectory in (app, Compile)).value,
    sourceDirectory in Compile := (sourceDirectory in (app, Compile)).value,
    sourceDirectory in Test := (sourceDirectory in (app, Test)).value,
    watchSources in Test := (watchSources in (app, Test)).value,
    sourceDirectory in IntegrationTest := (baseDirectory in app).value / "src" / "it",

    unmanagedResourceDirectories in Compile ++= Seq(
      (baseDirectory in app).value / "conf" / "base",
      (baseDirectory in app).value / "conf" / "secrets",
      (baseDirectory in app).value / "conf" / "test",
      (baseDirectory in app).value / "public"
    ),

    // Fork JVM when running tests
    fork in Test := true,
    fork in IntegrationTest := true,
    javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled"),
    javaOptions in IntegrationTest ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled"),
    parallelExecution in IntegrationTest := false,

    testOptions in IntegrationTest += Tests.Setup( () => ITHelper.startServer(baseDirectory.value.getAbsolutePath()) ),
    testOptions in IntegrationTest += Tests.Cleanup( () => ITHelper.shutdownServer() ),
    test in IntegrationTest := ((test in IntegrationTest) dependsOn (stage in Universal)).value,
    testOnly in IntegrationTest := ((testOnly in IntegrationTest) dependsOn (stage in Universal)).evaluated
  )

lazy val prodPackage = project
  .in(file("build/prod"))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .settings(
    commonSettings,

    resourceDirectory in Compile := (resourceDirectory in (app, Compile)).value,
    sourceDirectory in Compile := (sourceDirectory in (app, Compile)).value,

    unmanagedResourceDirectories in Compile ++= Seq(
      (baseDirectory in app).value / "conf" / "base",
      (baseDirectory in app).value / "conf" / "prod",
      (baseDirectory in app).value / "public"
    ),

    dockerExposedPorts := Seq(9000),
    dockerExposedVolumes := Seq("/opt/docker/logs")
  )