import java.util.Properties

import config.Application._
import model.{ForgotPasswordDto, RegisterDtoResponse, ExperienceDto, ConnectionInvitationDto}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

object UserConsumer extends App {

  val consumer = new KafkaConsumer[String, String](configuration)
  consumer.subscribe(List(signupTopic).asJava)
  
  val forgotTopicConsumer = new KafkaConsumer[String, String](configuration)
  forgotTopicConsumer.subscribe(List(forgotTopic).asJava)

  val connInvTopicConsumer = new KafkaConsumer[String, String](configuration)
  connInvTopicConsumer.subscribe(List(invitationTopic).asJava)
  
  val experienceTopicConsumer = new KafkaConsumer[String, String](configuration)
  experienceTopicConsumer.subscribe(List(userExperienceTopic).asJava)

  private def configuration: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
    //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "500")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props
  }


  while (true) {
    import model.MyJsonProtocol.registerDtoResponse
    import spray.json._
    import model.MyJsonProtocol.forgotPasswordDto
    import model.MyJsonProtocol.experienceDto
    import model.MyJsonProtocol.connectionInvitationDto
    

    val recordsConnInvite: ConsumerRecords[String, String] = connInvTopicConsumer.poll(1000)
    println("Hi")
    //println(recordsConnInvites)
    recordsConnInvite.asScala.foreach(record => {println(s" Received message: $recordsConnInvite")
      val userData = record.value().parseJson.convertTo[ConnectionInvitationDto]
      MailGun.sendInvitationMail(userData)
    })
    
    val records: ConsumerRecords[String, String] = consumer.poll(1000)
    records.asScala.foreach(record => {println(s"Register Received message: $record")
      val userData = record.value().parseJson.convertTo[RegisterDtoResponse]
      MailGun.sendMessage(userData)
    })


    val recordsNew: ConsumerRecords[String, String] = forgotTopicConsumer.poll(1000)
    //println("forgot")
    recordsNew.asScala.foreach(record => {println(s" Received message: $record")
      val userData = record.value().parseJson.convertTo[ForgotPasswordDto]
      println(userData)
      MailGun.sendForgotMail(userData)
    })

    
    val recordsExp: ConsumerRecords[String, String] = experienceTopicConsumer.poll(1000)
    //println("great")
    recordsExp.asScala.foreach(record => {println(s" Received message experience: $recordsExp")
      val userData = record.value().parseJson.convertTo[ExperienceDto]
      println(userData)
      SinkToDB.sendToNeo4j(userData)
      
    })
    
  }

}
