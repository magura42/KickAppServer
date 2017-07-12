package models

import play.api.libs.json.Json

object PushRegistration {

  case class PushRegistration(url: String, auth: String, key: String)

  implicit val userWrites = Json.writes[PushRegistration]
  implicit val userReads = Json.reads[PushRegistration]
}
