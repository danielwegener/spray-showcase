
import sbt._
import Keys._

object Dependencies {

  val akkaV = "2.2.3"
  val sprayV = "1.2.0"
  lazy val sprayWithAkkaDependencies  =
  Seq(
    "io.spray"            %   "spray-can"     % sprayV  withSources(),
    "io.spray"            %   "spray-routing" % sprayV  withSources(),
    "io.spray"            %   "spray-testkit" % sprayV  withSources(),
    "io.spray"            %   "spray-caching" % sprayV  withSources(),
    "io.spray"            %   "spray-json_2.10"     % "1.2.5"  withSources(),
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV  withSources(),
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV  withSources(),
    "org.specs2"          %%  "specs2"        % "2.2.3" % "test"
  )

}