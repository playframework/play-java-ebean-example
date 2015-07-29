name := "computer-database-java"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  "org.webjars" % "jquery" % "2.1.4",
  "org.webjars" % "bootstrap" % "3.3.5"
)     

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

routesGenerator := InjectedRoutesGenerator