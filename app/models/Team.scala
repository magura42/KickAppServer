package models

import play.api.libs.json.Json

object Team {

  case class Team(teamid: Int, name: String, fromyear: Int, toyear: Int,
                  foto: Option[Array[Byte]], clubid: Int, info: Option[String])

  implicit val userWrites = Json.writes[Team]
  implicit val userReads = Json.reads[Team]
}
