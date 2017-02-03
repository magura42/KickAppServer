package models

import play.api.libs.json.Json

object User {

  case class User(id: Int, firstname: String, lastname: String)

  implicit val userWrites = Json.writes[User]
  implicit val userReads = Json.reads[User]
}
