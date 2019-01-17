import config.Application._
import org.matthicks.mailgun._

import scala.concurrent._
import scala.concurrent.duration._

object MailGun {

  def sendMessage(message: String) = {
    val mailgun = new Mailgun(domainName, apiKey)
    val response = mailgun.send(Message.simple(
      from = EmailAddress("flavoursoft@yahoo.com", "Test App"),
      to = EmailAddress("flavoursoft@yahoo.com", "Joe User"),
      "Successfully Registered USer!",
      text = message,
      html = "<html><b>This</b> <i>seems</i> <img src=\"cid:example.jpg\"/> to <h1>work!</h1></html>"
    ))

    val result = Await.result(response, Duration.Inf)
    println(s"Result: $result")
  }
}
