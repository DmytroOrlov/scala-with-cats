import Dependencies._

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.10")
addCompilerPlugin(("org.scalameta" % "paradise" % "3.0.0-M11").cross(CrossVersion.full))

lazy val zioVersion = "1.0-RC4"

lazy val `scala-with-cats` = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.8",
      version := "0.1.0-SNAPSHOT"
    )),
    libraryDependencies ++= Seq(
      "org.scalaz" %% "scalaz-zio" % zioVersion,
      "org.scalaz" %% "scalaz-zio-interop-cats" % zioVersion,
      "org.typelevel" %% "cats-core" % "2.0.0-M1",
      "org.typelevel" %% "cats-mtl-core" % "0.5.0",
      "org.typelevel" %% "cats-tagless-macros" % "0.2.0",
      "io.monix" %% "monix" % "3.0.0-RC2",
      scalaTest % Test
    ),
    scalacOptions ++= Seq(
      "-Ypartial-unification"
//      , "-Xfatal-warnings"
    )
  )
