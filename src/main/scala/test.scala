import com.mashape.unirest.http.Unirest
import config.Application.{apiKey, fromEmailAddress}
import config.Application._
object test extends App{

  println("parsed message from topic invitation:" )

  //  val mailgun = new Mailgun(domainName, apiKey)
  //val headers = Map("h:X-Mailgun-Variables" -> message)

  val request = Unirest.post("https://api.mailgun.net/v3/" + domainName + "/messages")
    .basicAuth("api", apiKey)
    //.field("content-type",  "multipart/form-data;")
    .field("from", fromEmailAddress)
    .field("to", "flavoursoft@yahoo.com")
    .field("subject",  "test")
    .field("template", "connection_invitation")
    .field("h:X-Mailgun-Variables", "{\"inviteeName\":\"Rush\"}")
    .asJson()

 println( request.getBody())
}
