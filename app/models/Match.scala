package models

import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

import play.api.libs.json._

object Match {

  case class Match(matchid: Int, street: String, zipcode: String, city: String, date: Date, begintime: Time,
    endtime: Time, gettogethertime: Time, matchtype: String, teamid: Int)

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

  implicit val userWrites = Json.writes[Match]
  implicit val userReads = Json.reads[Match]
}

object MatchMaker {

  import models.Event.Event
  import models.Match.Match

  def apply(event: Event) = new Match(event.eventId, event.street, event.zipcode, event.city,
    event.date, event.begintime, event.endtime, event.gettogethertime, event.eventSubtype.get, event.teamId)
}

