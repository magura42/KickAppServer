package models

import play.api.libs.json.Json

object PushNotification {

  case class PushNotification(title: String, msg: String)

  implicit val userWrites = Json.writes[PushNotification]
  implicit val userReads = Json.reads[PushNotification]

}
