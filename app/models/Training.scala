package models

import java.sql.Time
import java.text.SimpleDateFormat
import java.sql.Date

import play.api.libs.json._

object Training {

  case class Training(trainingid: Int, street: String, zipcode: String, city: String, date: Date, begintime: Time,
                      endtime: Time, gettogethertime: Time, teamid: Int)

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

  implicit val userWrites = Json.writes[Training]
  implicit val userReads = Json.reads[Training]
}

object TrainingMaker {

  import models.Event.Event
  import models.Training.Training

  def apply(event: Event) = new Training(event.eventId, event.street, event.zipcode, event.city,
    event.date, event.begintime, event.endtime, event.gettogethertime, event.teamId)
}