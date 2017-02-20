package models

import play.api.libs.json.Json

object Trainingelement {

  case class Trainingelement(trainingelementid: Int, trainingid: Int, exerciseid: Int)

  implicit val userWrites = Json.writes[Trainingelement]
  implicit val userReads = Json.reads[Trainingelement]
}
