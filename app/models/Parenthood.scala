package models

import play.api.libs.json.Json

object Parenthood {

  case class Parenthood(parenthoodid: Int, parentid: Int, childid: Int)

  implicit val userWrites = Json.writes[Parenthood]
  implicit val userReads = Json.reads[Parenthood]
}
