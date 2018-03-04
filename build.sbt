import sbt.Keys._

lazy val rootProjectName = "akka-http-slick"

lazy val organizationName = "net.softler"

lazy val projectVersion = "0.0.1"

scalaVersion in ThisBuild := "2.12.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

enablePlugins(JavaServerAppPackaging)

lazy val library = new {

  object Version {
    val akka = "2.5.11"
    val akkaHttp = "10.0.11"
    val flyway = "3.2.1"
    val scalaTest = "3.0.4"
    val logbackVersion = "1.2.3"
    val slick = "3.2.1"
  }

  val logBack: ModuleID = "ch.qos.logback" % "logback-classic" % Version.logbackVersion

  val flyway: ModuleID = "org.flywaydb" % "flyway-core" % Version.flyway

  val scalaTest: ModuleID = "org.scalatest" %% "scalatest" % Version.scalaTest

  lazy val akka: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-actor" % Version.akka,
    "com.typesafe.akka" %% "akka-stream" % Version.akka,
    "com.typesafe.akka" %% "akka-slf4j" % Version.akka,
    "com.typesafe.akka" %% "akka-testkit" % Version.akka % Test
  )

  val akkaHttp: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-core" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-spray-json" % Version.akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % "test"
  )

  val slick: Seq[ModuleID] = Seq(
    "com.typesafe.slick" %% "slick" % Version.slick,
    "com.typesafe.slick" %% "slick-hikaricp" % Version.slick,
    "org.postgresql" % "postgresql" % "9.4.1211",
    "com.h2database" % "h2" % "1.4.192" % "test"
  )
}

lazy val resolver = Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
)

lazy val codeQualitySettings = Seq(
  ivyLoggingLevel := UpdateLogging.DownloadOnly,
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-Xfatal-warnings",
    "-Yno-adapted-args",
    "-Xfuture"
  ),
  scalastyleFailOnError := true,
  coverageEnabled in Test := true,
  coverageMinimum in Test := 80,
  coverageFailOnMinimum in Test := false,
  coverageHighlighting in Test := true
)

def commonSettings(projectName: String) =
  Seq(
    organization := organizationName,
    name := projectName,
    version := projectVersion,
    resolvers ++= resolver,
    libraryDependencies += library.scalaTest % Test
  ) ++ codeQualitySettings


lazy val akkaHttpSlickSample = (project in file("sample"))
  .settings(commonSettings("akka-http-slick-sample"))
  .settings(
    libraryDependencies ++= library.akka ++ library.akkaHttp ++ library.slick ++
      Seq(library.flyway, library.logBack),
    logBuffered := false
  )

lazy val root = (project in file("."))
  .settings(commonSettings(rootProjectName))
  .settings(
    aggregate in update := false,
    publishArtifact := false
  )
  .aggregate(akkaHttpSlickSample)
