package models

import play.api.libs.json.Json

object Person {

  case class Person(personid: Int, firstname: String, lastname: String, email: String, password: String, login: String, isAdmin: Boolean)

  implicit val userWrites = Json.writes[Person]
  implicit val userReads = Json.reads[Person]
}
