import config.Application._
import model.{ConnectionInvitationDto, ForgotPasswordDto, RegisterDtoResponse}
import org.matthicks.mailgun._

import scala.concurrent._
import scala.concurrent.duration._
import java.io._

import com.mashape.unirest.http.Unirest



object MailGun {

  def sendMessage(message: RegisterDtoResponse) = {
    println("parsed message from topic 1:" + message)
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
    /**
    println("parsed message from topic 2:" + message)
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
    */

    val request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
      .basicAuth("api", apiKey)
      //.field("content-type",  "multipart/form-data;")
      .field("from", ""+ fromEmailName +"  "+ fromEmailAddress +"")
      .field("to", "flavoursoft@yahoo.com") //message.inviteeEmail)
      .field("subject",  ForgotPasswordSubject)
      .field("template", "connection_invitation")
      .field("h:X-Mailgun-Variables", "{\"firstname\":\" " + message.firstName + " \", \"lastname\":\" " + message.lastName + " \"}")
      .asJson()

  }

  def sendInvitationMail(message: ConnectionInvitationDto) = {
    println("parsed message from topic invitation:" + message)
    
    val employerVal: String = message.employer match { case None => "" case Some(str) => str }
    val positionVal: String = message.position match { case None => "" case Some(str) => str }
    val stateVal: String = message.state match { case None => "" case Some(str) => str }
    val countryVal: String = message.country match { case None => "" case Some(str) => str }
    
    val request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
      .basicAuth("api", apiKey)
      //.field("content-type",  "multipart/form-data;")
      .field("from", ""+ message.firstname +" "+ message.lastname +"  "+ fromEmailAddressInvite +"")
      .field("to", "flavoursoft@yahoo.com") //message.inviteeEmail)
      .field("subject",  ""+ message.inviteeName +", please add me to your colony")
      .field("template", "connection_invitation")
      .field("h:X-Mailgun-Variables", "{\"inviteeName\":\" " + message.inviteeName +" \", \"firstname\":\" " + message.firstname + " \", \"lastname\":\" " + message.lastname + " \", \"avatar\":\" " + message.avatar + " \", \"email\":\" " + message.email + " \", \"state\":\" " + stateVal + " \", \"country\":\" " + countryVal + " \", \"employer\":\" " + employerVal + " \", \"position\":\" " + positionVal + " \", \"inviteeEmail\":\" " + message.inviteeEmail + " \"}")
      .asJson()

    //.field("h:X-Mailgun-Variables", "{\"inviteeName\":" + message.inviteeName + ", \"firstname\":" + message.firstname + ", \"lastname\":" + message.lastname + ", \"avatar\":" + message.avatar + ", \"position\":" + message.position + ", \"employer\":" + message.employer + ", \"country\":" + message.country + ", \"state\":" + message.state + ", \"inviteeEmail\":" + message.inviteeEmail + ", \"email\":" + message.email + "}")
    println( request.getBody())
    /** val result = Await.result(response, Duration.Inf)
    println(s"Result: $result")*/
  }

}


/**
   curl -s --user 'api:key-8439b6fada7f7dde0652d5564cff0fde' \
	 https://api.mailgun.net/v3/no-reply.careercolony.com/messages \
	 -F from='Mailgun Sandbox <postmaster@no-reply.careercolony.com>' \
	 -F to='Carl Njoku <flavoursoft@yahoo.com>' \
	 -F subject='Hello Carl Njoku' \
	 -F template='connection_invitation' \
	 -F h:X-Mailgun-Variables='{"firstname": "Chinedu", "lastname":"Njoku"}'*/