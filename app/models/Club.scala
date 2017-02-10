package models

import java.sql.Blob

import play.api.libs.json.Json

object Club {

  case class Club(clubid: Int, name: String, street: String, zipcode: String, city: String,
                  logo: Option[Array[Byte]], contact: Option[String], email: Option[String],
                  telefon: Option[String], web: Option[String])

  implicit val userWrites = Json.writes[Club]
  implicit val userReads = Json.reads[Club]
}
