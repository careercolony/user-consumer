package config

import com.typesafe.config.{Config, ConfigFactory}
import reactivemongo.api.{MongoConnection, MongoDriver}

import scala.concurrent.Future

object Application {
  val config: Config = ConfigFactory.load("application.conf")

  val signupTopic: String = config.getString("kafka.signupTopic")
  val forgotTopic: String = config.getString("kafka.forgotTopic")
  val userExperienceTopic: String = config.getString("kafka.userExperienceTopic")
  val invitationTopic: String = config.getString("kafka.invitationTopic")
  val brokers: String = config.getString("kafka.brokers")
  val groupId: String = config.getString("kafka.groupId")

  val domainName: String = config.getString("mailgun.domainName")
  val apiKey: String = config.getString("mailgun.apiKey")
  val emailTemplate: String = config.getString("mailgun.emailTemplate")
  val fromEmailAddress: String = config.getString("mailgun.fromEmailAddress")
  val fromEmailName: String = config.getString("mailgun.fromEmailName")
  
  val subject: String = config.getString("mailgun.RegisterSubject")
  val ForgotPasswordSubject: String = config.getString("mailgun.ForgotPasswordSubject")
  val ContactInvitationSubject: String = config.getString("mailgun.ContactInvitationSubject")
 // val InviteVariables: String = config.getString("mailgun.Variables")

  
  


  //neo4j config
  val neo4jUrl = config.getString("neo4j.url")
  val neo4jUsername = config.getString("neo4j.username")
  var neo4jPassword = config.getString("neo4j.password")
}

