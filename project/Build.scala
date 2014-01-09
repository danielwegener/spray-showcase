import sbt._
import Keys._

object Build extends Build {

  import BuildSettings._
  import Dependencies._


  lazy val root = Project(id = "spray-showcase", base = file("."))
    .aggregate(`spray-http-example`, `spray-routing-example`)

  lazy val `spray-http-example` = Project(id = "spray-http-example", base = file("spray-http-example"))
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

  lazy val `spray-routing-example` = Project(id = "spray-routing-example", base = file("spray-routing-example"))
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

}