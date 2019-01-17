package config

import com.typesafe.config.{Config, ConfigFactory}
import reactivemongo.api.{MongoConnection, MongoDriver}

import scala.concurrent.Future

object Application {
  val config: Config = ConfigFactory.load("application.conf")

  val topic: String = config.getString("kafka.topic")
  val brokers: String = config.getString("kafka.brokers")
  val groupId: String = config.getString("kafka.groupId")

  val domainName: String = config.getString("mailgun.domainName")
  val apiKey: String = config.getString("mailgun.apiKey")
}
