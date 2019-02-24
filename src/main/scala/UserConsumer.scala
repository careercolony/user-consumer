import java.util.Properties

import config.Application._
import model.{ForgotPasswordDto, RegisterDtoResponse}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

import scala.collection.JavaConverters._

object UserConsumer extends App {

  val consumer = new KafkaConsumer[String, String](configuration)
  consumer.subscribe(List(signupTopic).asJava)

  private def configuration: Properties = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getCanonicalName)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
    props
  }

  val forgotTopicConsumer = new KafkaConsumer[String, String](configuration)
  forgotTopicConsumer.subscribe(List(forgotTopic).asJava)

  while (true) {
    import model.MyJsonProtocol.forgotPasswordDto
    import spray.json._

    val records: ConsumerRecords[String, String] = forgotTopicConsumer.poll(1000)
    records.asScala.foreach(record => {println(s" Received message: $record")
      val userData = record.value().parseJson.convertTo[ForgotPasswordDto]
      MailGun.sendForgotMail(userData)
    })
  }

  while (true) {
    import model.MyJsonProtocol.registerDtoResponse
    import spray.json._

    val records: ConsumerRecords[String, String] = consumer.poll(1000)
    records.asScala.foreach(record => {println(s"Register Received message: $record")
      val userData = record.value().parseJson.convertTo[RegisterDtoResponse]
      MailGun.sendMessage(userData)
    })
  }



}
