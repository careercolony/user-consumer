package model

import spray.json.DefaultJsonProtocol

case class RegisterDtoResponse(memberID: String, firstname: String, lastname: String, email: String ,  avatar: String)

case class ForgotPasswordDto(email: String, firstName : String , lastName : String)

case class ConnectionInvitationDto(memberID: String, firstname: String, lastname: String, email: String , avatar: String, position : Option[String], employer : Option[String], country:Option[String], state:Option[String], inviteeEmail : String, inviteeName : String)

case class ExperienceDto(memberID: String, employer:Option[String], position:Option[String], career_level: String, 
  start_month: Option[String], created_date: String, description: Option[String],
  industry: Option[String], expID: String, start_year: Option[String], status : String, end_year: Option[String],
  end_month: Option[String], current:Boolean
)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val registerDtoResponse = jsonFormat5(RegisterDtoResponse)
  implicit val forgotPasswordDto = jsonFormat3(ForgotPasswordDto)
  implicit val experienceDto = jsonFormat14(ExperienceDto)
  implicit val connectionInvitationDto = jsonFormat11(ConnectionInvitationDto)
  
}
