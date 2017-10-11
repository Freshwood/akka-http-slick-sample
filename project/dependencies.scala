import sbt._

object Versions {
  val akka = "2.5.4"
  val akkaHttp = "10.0.10"
  val flyway = "3.2.1"
  val scalaTest = "3.0.1"
  val logbackVersion = "1.2.3"
  val slick = "3.2.1"
}

object Dependencies {

  val logBack: ModuleID = "ch.qos.logback" % "logback-classic" % Versions.logbackVersion

  val flyway: ModuleID = "org.flywaydb" % "flyway-core" % Versions.flyway

  val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Versions.scalaTest % "test"

  lazy val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % Versions.akka,
    "com.typesafe.akka" %% "akka-stream" % Versions.akka,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.akka,
    "com.typesafe.akka" %% "akka-testkit" % Versions.akka % Test
  )

  val akkaHttp: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-core" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % Versions.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Versions.akkaHttp % "test"
  )

  val slick: Seq[ModuleID] = Seq(
    "com.typesafe.slick" %% "slick" % Versions.slick,
    "com.typesafe.slick" %% "slick-hikaricp" % Versions.slick,
    "org.postgresql" % "postgresql" % "9.4.1211",
    "com.h2database" % "h2" % "1.4.192" % "test"
  )
}
