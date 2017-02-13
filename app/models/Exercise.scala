package models

import java.util.Date

import dao.Exercisetype
import dao.Exercisetype.Exercisetype
import play.api.libs.json._

object Exercise {

  case class Exercise(exerciseid: Int, name: String, exercisetype: Exercisetype, setup: String, execution: String,
                      variants: Option[String], graphic: Option[Array[Byte]], note: Option[String])

  implicit val exercisetypeFormat = new Format[Exercisetype] {

    def reads(js: JsValue): JsResult[Exercisetype] = {
      js.validate[String] fold (
        error => JsError(error),
        exercisetype => exercisetype match {
          case "warmup" => JsSuccess(Exercisetype.warmup)
          case "shot" => JsSuccess(Exercisetype.shot)
          case "pass" => JsSuccess(Exercisetype.pass)
          case "trick" => JsSuccess(Exercisetype.trick)
          case "duel" => JsSuccess(Exercisetype.duel)
          case "goalkeeper" => JsSuccess(Exercisetype.goalkeeper)
          case "header" => JsSuccess(Exercisetype.header)
          case "hall" => JsSuccess(Exercisetype.hall)
          case "freeplay" => JsSuccess(Exercisetype.freeplay)
          case "feeling" => JsSuccess(Exercisetype.feeling)
          case _ => JsError(Nil) // Should probably contain some sort of `ValidationError`
        }
        )
    }

    def writes(r: Exercisetype): JsValue = JsString(r.toString)
  }

  implicit val userWrites = Json.writes[Exercise]
  implicit val userReads = Json.reads[Exercise]
}