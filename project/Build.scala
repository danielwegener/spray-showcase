import sbt._
import Keys._

object Build extends Build {

  import BuildSettings._
  import Dependencies._


  lazy val root = project.in(file("."))
    .aggregate(`spray-http-example`, `spray-routing-example`, slides, `spray-tcp-server-example`)

  lazy val `spray-tcp-server-example` = project
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

  lazy val `spray-http-example` = project
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

  lazy val `spray-routing-example` = project
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= sprayWithAkkaDependencies)

  lazy val `spray-client-example` = project
    .settings(formatSettings: _*)
    .settings(libraryDependencies ++= (sprayWithAkkaDependencies ++ Seq(
    "io.spray"  %   "spray-client"  % sprayV withSources(),
    "com.typesafe.akka"                       %%  "akka-slf4j"                  % "2.1.4",
    "ch.qos.logback"                          %   "logback-classic"             % "1.0.13"
  )))

  lazy val slides = project
    //todo: add grunt plugin
    //.settings(SbtGruntTask.gruntTaskSettings)


}