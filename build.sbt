import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "TableFileLoader",
    libraryDependencies ++= Seq( 
      "org.scalatest" %% "scalatest" % "3.2.10" % Test, 
      "org.apache.commons" % "commons-compress" % "1.21", 
      "org.apache.commons" % "commons-io" % "1.3.2" , 
      "com.github.junrar" % "junrar" % "0.7"
      )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
