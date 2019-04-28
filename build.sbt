enablePlugins(JavaAppPackaging)
enablePlugins(AshScriptPlugin)


packageName in Docker := "careercolony/akka-http-docker-userconsumer-api"
dockerBaseImage := "openjdk:jre-alpine"


name := "UserConsumer"


version := "1.0"

scalaVersion := "2.11.6"

val akkaExp = "1.0-RC4" // "2.0.5"
val akkaHttpV = "10.1.6"
val akkaV = "2.3.14"
libraryDependencies ++= {
  val akkaV = "2.5.16"
  val akkaHttpV = "10.1.5"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.4.0",
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.0-M1",
    "io.spray" %%  "spray-json" % "1.3.5",
    "com.typesafe.akka" %% "akka-slf4j" % "2.4.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.0-M1",
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % "2.0-M1" % "test",
    "com.typesafe.akka" %% "akka-testkit" % "2.4.0" % "test",
    "org.scalatest" %% "scalatest" % "2.2.1" % "test",
    "org.reactivemongo" %% "reactivemongo" %  "0.12.7",
    "org.reactivemongo" %% "reactivemongo-play-json" %  "0.12.7-play26",
    "joda-time" % "joda-time" % "2.8.1",
    "org.neo4j.driver" % "neo4j-java-driver" % "1.0.4",
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.11-M2",
    "com.typesafe.akka" %% "akka-cluster" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
    "org.matthicks" %% "mailgun4s" % "1.0.9",
     "org.neo4j.driver" % "neo4j-java-driver" % "1.0.4",
    "com.mashape.unirest" % "unirest-java" % "1.4.9",
    "org.apache.httpcomponents" % "httpclient" % "4.3.6",
    "org.apache.httpcomponents" % "httpasyncclient" % "4.0.2",
    "org.apache.httpcomponents" % "httpmime" % "4.3.6",
    "org.json" % "json" % "20140107"
  )
}
