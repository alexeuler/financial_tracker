val http4sVersion = "0.17.0-M2"
val doobieVersion = "0.4.1"
val circeVersion = "0.7.1"

lazy val commonSettings = Seq(
    organization := "com.financetracker",
    scalaVersion := "2.12.2",
    version := "0.1.7",
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

    // // Fork JVM when running tests
    // fork in Test := true,
    // javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled"),
    // // Sbt native packager
    // dockerExposedPorts := Seq(9000),
    // dockerExposedVolumes := Seq("/opt/docker/logs"),

    // testOptions in IntegrationTest += Tests.Setup( () => println("Setup") ),
    // testOptions in IntegrationTest += Tests.Cleanup( () => println("Cleanup") ),
    // test in IntegrationTest := ((test in IntegrationTest) dependsOn (stage in Universal)).value
    // // Add .conf files to resources (specific for tests)
    // unmanagedResourceDirectories in Test ++= Seq(
    //   baseDirectory.value / "conf" / "base",
    //   baseDirectory.value / "conf" / "secrets",
    //   baseDirectory.value / "conf" / "test",
    //   baseDirectory.value / "public"
    // ),

    // // Add .conf files to resources (specific for integration tests)
    // unmanagedResourceDirectories in IntegrationTest ++= Seq(
    //   baseDirectory.value / "conf" / "base",
    //   baseDirectory.value / "conf" / "secrets",
    //   baseDirectory.value / "conf" / "test",
    //   baseDirectory.value / "public"
    // ),

  // .configs(IntegrationTest)
  // .enablePlugins(JavaAppPackaging)
  // .enablePlugins(DockerPlugin)

    // libraryDependencies ++= Seq(
    //   "org.scalatest" %% "scalatest" % "3.0.3" % "it, test",
    //   "org.scalacheck" %% "scalacheck" % "1.13.5" % "it, test",
    //   "org.tpolecat" %% "doobie-scalatest-cats" % doobieVersion % "it, test",
    //   "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % "it, test"
    // ),

    // Defaults.itSettings,

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
    sourceDirectory in IntegrationTest := (baseDirectory in app).value / "src" / "it",

    unmanagedResourceDirectories in Compile ++= Seq(
      (baseDirectory in app).value / "conf" / "base",
      (baseDirectory in app).value / "conf" / "secrets",
      (baseDirectory in app).value / "conf" / "test",
      (baseDirectory in app).value / "public"
    ),

    // Fork JVM when running tests
    fork in Test := true,
    javaOptions in Test ++= Seq("-Xms512M", "-Xmx2048M", "-XX:+CMSClassUnloadingEnabled"),

    testOptions in IntegrationTest += Tests.Setup( () => println("Setup") ),
    testOptions in IntegrationTest += Tests.Cleanup( () => println("Cleanup") ),
    test in IntegrationTest := ((test in IntegrationTest) dependsOn (stage in Universal)).value
  )