package models

import dao.{Exercisetype, Teamtype}
import dao.Exercisetype.Exercisetype
import dao.Teamtype.Teamtype
import play.api.libs.json._

object Exercise {

  case class Exercise(exerciseid: Int, name: String, exercisetype: Exercisetype, teamtype: Teamtype,
                      setup: String, execution: String,
    variants: Option[String], graphic: Option[String], note: Option[String])

  implicit val exercisetypeFormat = new Format[Exercisetype] {

    def reads(js: JsValue): JsResult[Exercisetype] = {
      js.validate[String] fold(
        error => JsError(error),
        exercisetype => exercisetype match {
          case "warmup" => JsSuccess(Exercisetype.warmup)
          case "shot" => JsSuccess(Exercisetype.shot)
          case "pass" => JsSuccess(Exercisetype.pass)
          case "trick" => JsSuccess(Exercisetype.trick)
          case "duel" => JsSuccess(Exercisetype.duel)
          case "goalkeeper" => JsSuccess(Exercisetype.goalkeeper)
          case "header" => JsSuccess(Exercisetype.header)
          case "indoor" => JsSuccess(Exercisetype.indoor)
          case "freeplay" => JsSuccess(Exercisetype.freeplay)
          case "feeling" => JsSuccess(Exercisetype.feeling)
          case _ => JsError(Nil) // Should probably contain some sort of `ValidationError`
        }
        )
    }

    def writes(r: Exercisetype): JsValue = JsString(r.toString)
  }

  implicit val teamtypeFormat = new Format[Teamtype] {
  def reads(js: JsValue): JsResult[Teamtype] = {
    js.validate[String] fold(
      error => JsError(error),
      teamtype => teamtype match {
        case "bambini" => JsSuccess(Teamtype.Bambini)
        case "f" => JsSuccess(Teamtype.F)
        case "e" => JsSuccess(Teamtype.E)
        case "d" => JsSuccess(Teamtype.D)
        case "c" => JsSuccess(Teamtype.C)
        case "b" => JsSuccess(Teamtype.B)
        case "a" => JsSuccess(Teamtype.A)
        case "senior" => JsSuccess(Teamtype.Senior)
        case "ah" => JsSuccess(Teamtype.AH)
        case _ => JsError(Nil) // Should probably contain some sort of `ValidationError`
      }
    )
  }

  def writes(r: Teamtype): JsValue = JsString(r.toString)
}

  implicit val userWrites = Json.writes[Exercise]
  implicit val userReads = Json.reads[Exercise]
}
