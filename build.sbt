name := """CRM"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaJpa,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m"
)

libraryDependencies += "org.webjars" % "bootstrap" % "3.2.0"

libraryDependencies += "org.apache.directory.studio" % "org.apache.commons.io" % "2.4"