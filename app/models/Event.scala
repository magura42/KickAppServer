package models

import java.sql.Date
import java.sql.Time
import java.text.SimpleDateFormat

import models.Tournament.Tournament
import play.api.libs.json._

import scala.collection.mutable.ListBuffer

object Event {

  case class Event(eventId: Int, street: String, zipcode: String, city: String, date: Date, begintime: Time,
    endtime: Time, gettogethertime: Time, contact: Option[String], email: Option[String],
    telefon: Option[String], web: Option[String], participationYes: ListBuffer[Int],
    participationNo: ListBuffer[Int], participationMaybe: ListBuffer[Int])

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

  implicit val userWrites = Json.writes[Event]
  implicit val userReads = Json.reads[Event]


}

object EventMaker {

  import models.Event.Event
  import models.Training.Training

  def apply(training: Training) = new Event(training.trainingid, training.street, training.zipcode, training.city,
    training.date, training.begintime, training.endtime, training.gettogethertime, None, None, None, None,
    ListBuffer[Int](), ListBuffer[Int](), ListBuffer[Int]())

  def apply(tournament: Tournament) = new Event(tournament.tournamentid, tournament.street, tournament.zipcode,
    tournament.city,
    tournament.date, tournament.begintime, tournament.endtime, tournament.gettogethertime, tournament.contact,
    tournament.email, tournament.telefon, tournament.web, ListBuffer[Int](), ListBuffer[Int](), ListBuffer[Int]())
}