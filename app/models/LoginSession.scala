package models

import dao.Role
import dao.Role.Role
import play.api.libs.json.Format
import play.api.libs.json.JsError
import play.api.libs.json.JsResult
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.Json

object LoginSession {

  case class LoginSession(personid: Int, personname: String, role: Role, clubid: Int, clubname: String, teamid: Int, teamname: String)

  implicit val roleFormat = new Format[Role] {

    def reads(js: JsValue): JsResult[Role] = {
      js.validate[String] fold(
        error => JsError(error),
        role => role match {
          case "coach" => JsSuccess(Role.coach)
          case "player" => JsSuccess(Role.player)
          case "parent" => JsSuccess(Role.parent)
          case _ => JsError(Nil) // Should probably contain some sort of `ValidationError`
        }
        )
    }

    def writes(r: Role): JsValue = JsString(r.toString)
  }
  implicit val userWrites = Json.writes[LoginSession]
  implicit val userReads = Json.reads[LoginSession]
}
