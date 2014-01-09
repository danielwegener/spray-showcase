name := "examples"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= {
  val akkaV = "2.2.3"
  val sprayV = "1.2.0"
  Seq(
    "io.spray"            %   "spray-can"     % sprayV  withSources(), 
    "io.spray"            %   "spray-routing" % sprayV  withSources(),
    "io.spray"            %   "spray-testkit" % sprayV  withSources(),
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV  withSources(),
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV  withSources(),
    "org.specs2"          %%  "specs2"        % "2.2.3" % "test"
  )
}

