package models

import java.sql.Blob

import play.api.libs.json.Json

object Club {

  case class Club(clubid: Int, name: String, street: String, zipcode: String, city: String,
                  logo: Array[Byte], contact: String, email: String, telefon: String, web: String)

  implicit val userWrites = Json.writes[Club]
  implicit val userReads = Json.reads[Club]
}
