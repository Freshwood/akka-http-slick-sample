import sbt.Keys._

lazy val rootProjectName = "akka-http-slick"

lazy val organizationName = "net.softler"

lazy val projectVersion = "0.0.1"

scalaVersion in ThisBuild := "2.12.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

enablePlugins(JavaServerAppPackaging)

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
    "UTF-8", // yes, this is 2 args
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
    updateOptions := updateOptions.value.withLatestSnapshots(true),
    // Activate this, when the dependencies need to be compiled very often
    updateOptions := updateOptions.value.withCachedResolution(true),
    libraryDependencies += Dependencies.scalaTest
  ) ++ codeQualitySettings


lazy val akkaHttpSlickSample = (project in file("sample"))
  .settings(commonSettings("akka-http-slick-sample"))
  .settings(
    libraryDependencies ++= Dependencies.akka ++ Dependencies.akkaHttp ++ Dependencies.slick ++
      Seq(Dependencies.flyway, Dependencies.logBack),
    logBuffered := false
  )

lazy val root = (project in file("."))
  .settings(commonSettings(rootProjectName))
  .settings(
    aggregate in update := false,
    publishArtifact := false
  )
  .aggregate(akkaHttpSlickSample)
