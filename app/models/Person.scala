package models

import java.util.Date

import dao.Role
import dao.Role.Role
import play.api.libs.json._

object Person {

  case class Person(personid: Int, firstname: String, lastname: String, street: String, zipcode: String, city: String,
                    telephone: Option[String], email: Option[String], birthday: Option[Date],
                    login: String, password: String, role: Role, teamid: Option[Int], passnumber: Option[Int])

  implicit val roleFormat = new Format[Role] {

    def reads(js: JsValue): JsResult[Role] = {
      js.validate[String] fold (
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

  implicit val userWrites = Json.writes[Person]
  implicit val userReads = Json.reads[Person]
}
