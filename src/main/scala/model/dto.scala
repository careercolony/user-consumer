package model

import spray.json.DefaultJsonProtocol

case class RegisterDtoResponse(memberID: String, firstname: String, lastname: String, email: String ,  avatar: String)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val registerDtoResponse = jsonFormat5(RegisterDtoResponse)
}
