package models

import play.api.libs.json.Json

object Authentication {

  case class Authentication(username: String, password: String)

  implicit val userWrites = Json.writes[Authentication]
  implicit val userReads = Json.reads[Authentication]
}
