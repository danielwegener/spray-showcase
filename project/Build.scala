import sbt._
import Keys._

object Build extends Build {

  import BuildSettings._
  import Dependencies._


  lazy val root = project.in(file("."))
    .aggregate(`spray-http-example`, `spray-routing-example`, slides)

  lazy val `spray-http-example` = project
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

  lazy val `spray-routing-example` = project
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

  lazy val slides = project
    //todo: add grunt plugin
    //.settings(SbtGruntTask.gruntTaskSettings)


}