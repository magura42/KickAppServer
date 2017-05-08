package models

import play.api.libs.json.Json

object Participant {

  case class Participant(participantId: Int, name: String)

  implicit val userWrites = Json.writes[Participant]
  implicit val userReads = Json.reads[Participant]
}
