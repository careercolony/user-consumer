import config.Application._
import model.{ConnectionInvitationDto, ForgotPasswordDto, RegisterDtoResponse}
import org.matthicks.mailgun._

import scala.concurrent._
import scala.concurrent.duration._
import java.io._

import com.mashape.unirest.http.Unirest



object MailGun {

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
      .field("to", ""+ message.inviteeEmail +"") //message.inviteeEmail)
      .field("subject",  ""+ message.inviteeName +", please add me to your colony")
      .field("template", "connection_invitation")
      .field("h:X-Mailgun-Variables", "{\"inviteeName\":\" " + message.inviteeName +" \", \"firstname\":\" " + message.firstname + " \", \"lastname\":\" " + message.lastname + " \", \"avatar\":\" " + message.avatar + " \", \"email\":\" " + message.email + " \", \"state\":\" " + stateVal + " \", \"country\":\" " + countryVal + " \", \"employer\":\" " + employerVal + " \", \"position\":\" " + positionVal + " \", \"inviteeEmail\":\" " + message.inviteeEmail + " \"}")
      .asJson()

    //println( request.getBody())
    
  }

  def sendMessage(message: RegisterDtoResponse) = {
    val request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
      .basicAuth("api", apiKey)
      //.field("content-type",  "multipart/form-data;")
      .field("from", ""+ fromEmailName +"  "+ fromEmailAddress +"")
      .field("to", ""+ message.email +"") //message.inviteeEmail)
      .field("subject",  RegisterSubject)
      .field("template", "signup")
      .field("h:X-Mailgun-Variables", "{\"firstname\":\" " + message.firstname + " \", \"lastname\":\" " + message.lastname + " \"}")
      .asJson()
  }

  def sendForgotMail(message: ForgotPasswordDto) = {
    
    val request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
      .basicAuth("api", apiKey)
      //.field("content-type",  "multipart/form-data;")
      .field("from", ""+ fromEmailName +"  "+ fromEmailAddress +"")
      .field("to", ""+ message.email +"")
      .field("subject",  ForgotPasswordSubject)
      .field("template", "password_reset")
      .field("h:X-Mailgun-Variables", "{\"firstname\":\" " + message.firstName + " \", \"lastname\":\" " + message.lastName + " \"}")
      .asJson()

  }
/**
  def sendPasswordChangedMail(message: ForgotPasswordDto) = {
    
    val request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
      .basicAuth("api", apiKey)
      //.field("content-type",  "multipart/form-data;")
      .field("from", ""+ fromEmailName +"  "+ fromEmailAddress +"")
      .field("to", "flavoursoft@yahoo.com") //message.inviteeEmail)
      .field("subject",  ForgotPasswordSubject)
      .field("template", "password_reset")
      .field("h:X-Mailgun-Variables", "{\"firstname\":\" " + message.firstName + " \", \"lastname\":\" " + message.lastName + " \"}")
      .asJson()

  }
*/
  


}



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




    