package model

import spray.json.DefaultJsonProtocol

case class RegisterDtoResponse(memberID: String, firstname: String, lastname: String, email: String ,  avatar: String)

case class ForgotPasswordDto(email: String)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val registerDtoResponse = jsonFormat5(RegisterDtoResponse)
  implicit val forgotPasswordDto = jsonFormat1(ForgotPasswordDto)
}
