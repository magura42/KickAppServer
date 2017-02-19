package models

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.json._

object Training {

  case class Training(trainingid: Int, street: String, zipcode: String, city: String, date: Date, begintime: Time,
                      endtime: Time, gettogethertime: Time)

  implicit object timeFormat extends Format[Time] {
    val format = new SimpleDateFormat("HH:mm:ss")
    def reads(json: JsValue) = {
      val str = json.as[String]
      JsSuccess(Time.valueOf(str))
    }
    def writes(ts: Time) = JsString(ts.toString)
  }

  implicit val userWrites = Json.writes[Training]
  implicit val userReads = Json.reads[Training]
}
