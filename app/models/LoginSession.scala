package models

import play.api.libs.json.Json

object LoginSession {

  case class LoginSession(personid: Int, personname: String, clubid: Int, clubname: String, teamid: Int, teamname: String)
  //case class LoginSession(personid: Int, personname: String, teamid: Int, teamname: String)
  implicit val userWrites = Json.writes[LoginSession]
  implicit val userReads = Json.reads[LoginSession]
}
