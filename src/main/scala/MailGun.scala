import config.Application._
import model.{ForgotPasswordDto, RegisterDtoResponse}
import org.matthicks.mailgun._

import scala.concurrent._
import scala.concurrent.duration._

object MailGun {


  def sendMessage(message: RegisterDtoResponse) = {
    println("parsed message from topic:" + message)
    val mailgun = new Mailgun(domainName, apiKey)
    val response = mailgun.send(Message.simple(
      from = EmailAddress(fromEmailAddress, fromEmailName),
      to = EmailAddress(message.email, message.firstname),
      //to = EmailAddress("rushabh_11490@yahoo.co.in", message.firstname),
      subject,
      html = "<html><body><div><br><br> Dear " + message.firstname + ", <br><b><h2> <font color=#32CD32>Congratulations! Your account has been created successfully!!</font></h2></b><br> Please login to the Carrier Colony website using the Login Id: " + message.email + "<br><br><br> Thank you for registering with Carrier Colony.<br><br><b>Best regards,</b><br> Carrier Colony Team<br><br>Note: This is a system generated e-mail, please do not reply to it.<br><br><br></body></html>"
    ))

    val result = Await.result(response, Duration.Inf)
    println(s"Result: $result")
  }

  def sendForgotMail(message: ForgotPasswordDto) = {
    println("parsed message from topic:" + message)
    val mailgun = new Mailgun(domainName, apiKey)
    val response = mailgun.send(Message.simple(
      from = EmailAddress(fromEmailAddress, fromEmailName),
      to = EmailAddress(message.email, message.email),
      //to = EmailAddress("rushabh_11490@yahoo.co.in","rushabh_11490@yahoo.co.in"),
      ForgotPasswordSubject,
      html = "<html><body><div><br><br> Dear " + message.email + ", <br><b><h2> Carrier Colony Team recently received a request for a forgotten password.</h2></b><br> To change your Carrier Colony password, please click on this   <font color=#0000ff><a>link</a></font> .<br><br><br> If you did not request this change, you do not need to do anything.<br><br><br> This link will expire in 4 hours.<br><br>\n<b>Best regards,</b><br> Carrier Colony Team <br><br><br></body></html>"
    ))

    val result = Await.result(response, Duration.Inf)
    println(s"Result: $result")
  }

}
