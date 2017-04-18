package models

import dao.Role
import dao.Role.Role
import play.api.libs.json._


object Trainingparticipant {

  case class Trainingparticipant(trainingparticipantid: Int, participantid: Int, trainingid: Int, role: Role,
    participantstatus: String)

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

  implicit val userWrites = Json.writes[Trainingparticipant]
  implicit val userReads = Json.reads[Trainingparticipant]
}
