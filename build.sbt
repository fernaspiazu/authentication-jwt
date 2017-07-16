name := """authentication-jwt"""

version := "0.1.0"

scalaVersion := "2.12.2"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers ++= Seq(
  Resolver.sonatypeRepo("public"),
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.bintrayRepo("scalameta", "maven"),
  Resolver.bintrayRepo("scalaz", "releases")
)

libraryDependencies ++= Seq(
  guice,
  "com.pauldijou"          %% "jwt-play"           % "0.14.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
)

scalafmtVersion in ThisBuild := "1.1.0"

scalafmtOnCompile in ThisBuild := true // all projects
