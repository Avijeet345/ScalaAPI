name := "ScalaAPI"

version := "0.1"
packageName in Docker := "myapi"
scalaVersion := "2.13.4"
libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream" % "2.6.10",
  "com.typesafe.akka" %% "akka-http" % "10.2.2",
  "org.json4s" %% "json4s-jackson" % "3.7.0-M7",
  "org.json4s" %% "json4s-native" % "3.7.0-M7",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "ch.rasc" % "bsoncodec" % "1.0.1",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",
 // "de.heikoseeberger" %% "akka-http-json4s" % "1.22.0",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.1.1"
)
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
