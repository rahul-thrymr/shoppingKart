name := """play_shoppingcartapp"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "javax.mail" % "mail" % "1.4.5",
  "com.itextpdf" % "itextpdf" % "5.0.6",
  "com.itextpdf.tool" % "xmlworker" % "5.5.6",
  "org.apache.poi" % "poi" % "3.13",
  "org.apache.poi" % "poi-ooxml" % "3.13"

)

libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1100-jdbc41"

lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
 