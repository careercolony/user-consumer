import config.Application._
import model.RegisterDtoResponse
import org.matthicks.mailgun._

import scala.concurrent._
import scala.concurrent.duration._

object MailGun {

  def sendMessage(message: RegisterDtoResponse) = {
    println("parsed message from topic:"+message)
    val mailgun = new Mailgun(domainName, apiKey)
    val response = mailgun.send(Message.simple(
      from = EmailAddress(fromEmailAddress,fromEmailName),
      to = EmailAddress(message.email, message.firstname),
      //to = EmailAddress("rushabh_11490@yahoo.co.in", message.firstname),
      subject,
      html = "<html><body><div><br><br> Dear "+ message.firstname +", <br><br><b> Congratulations! Your account has been created successfully!!</b><br><br> Please login to the Carrier Colony website using the Login Id: "+ message.email +"<br><br><br> Thank you for registering with Carrier Colony.<br><br><b>Best regards,</b><br> Carrier Colony Team<br><br>Note: This is a system generated e-mail, please do not reply to it.<br><br><br></body></html>"
    ))

    val result = Await.result(response, Duration.Inf)
    println(s"Result: $result")
  }
}
