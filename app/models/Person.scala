package models

import java.sql.Date

import dao.Role
import dao.Role.Role
import play.api.libs.json._

object Person {

  case class Person(personid: Int, firstname: String, lastname: String, street: String, zipcode: String, city: String,
                    telephone: Option[String], email: Option[String], birthday: Option[Date],
                    login: String, password: String, role: Role, teamid: Int, passnumber: Option[Int])

  implicit object dateFormat extends Format[Date] {
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(Date.valueOf(str))
    }
    def writes(ts: Date) = JsString(ts.toString)
  }

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
