val V = new {
  val zio = "1.0.3"
  val kindProjector = "0.11.1"
}

lazy val `scala-with-cats` = (project in file(".")).
  settings(
    inThisBuild(Seq(
      scalaVersion := "2.13.4",
      version := "0.1.0-SNAPSHOT",
      organization := "com.github.DmytroOrlov"
    )),
    addCompilerPlugin("org.typelevel" %% "kind-projector" % V.kindProjector cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % V.zio,
      "org.typelevel" %% "cats-core" % "2.2.0",
      "org.typelevel" %% "cats-mtl-core" % "0.7.1",
      "org.typelevel" %% "cats-tagless-macros" % "0.12",
      "io.monix" %% "monix" % "3.3.0",
      "org.scalatest" %% "scalatest" % "3.2.3" % Test,
      "org.scalacheck" %% "scalacheck" % "1.15.1" % Test,
    ),
    scalacOptions ++= Seq(
      "-Ymacro-annotations",
      // "-Xfatal-warnings",
    )
  )
