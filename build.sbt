lazy val akkaVersion     = "2.6.19"
lazy val akkaHttpVersion = "10.2.9"

lazy val commonSettings = Seq(
  organization := "com.firstbird",
  name         := "akka-persistence-typed-scala-usergroup",
  version      := "1.0",
  scalaVersion := "2.13.8",
  fork         := true
)

lazy val rootProject = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka"  %% "akka-actor-typed"             % akkaVersion,
      "com.typesafe.akka"  %% "akka-persistence-typed"       % akkaVersion,
      "com.typesafe.akka"  %% "akka-cluster-sharding-typed"  % akkaVersion,
      "com.typesafe.akka"  %% "akka-stream"                  % akkaVersion,
      "com.lightbend.akka" %% "akka-projection-eventsourced" % "1.2.3",
      "com.typesafe.akka"  %% "akka-http-spray-json"         % akkaHttpVersion,
      "com.typesafe.akka"  %% "akka-http"                    % akkaHttpVersion,
      "ch.qos.logback"      % "logback-classic"              % "1.2.3"
    )
  )
