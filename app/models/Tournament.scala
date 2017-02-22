package models

import java.sql.{Date, Time}
import java.text.SimpleDateFormat

import play.api.libs.json._

object Tournament {

  case class Tournament(tournamentid: Int, street: String, zipcode: String, city: String, date: Date, begintime: Time,
                        endtime: Time, gettogethertime: Time, contact: Option[String], email: Option[String],
                        telefon: Option[String], web: Option[String])

  implicit object timeFormat extends Format[Time] {
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(Time.valueOf(str))
    }
    def writes(ts: Time) = JsString(ts.toString)
  }

  implicit object dateFormat extends Format[Date] {
    val format = new SimpleDateFormat("dd.MM.yyyy")

    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(Date.valueOf(str))
    }

    def writes(ts: Date) = JsString(ts.toString)
  }

  implicit val userWrites = Json.writes[Tournament]
  implicit val userReads = Json.reads[Tournament]
}
