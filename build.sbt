import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "de.codecentric",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.typelevel" %% "spire" % "0.14.1",
    libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.0-RC1"
  )
