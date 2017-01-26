package controllers

import play.api.libs.json._
import play.api.mvc._
import models.User._

object UserController extends Controller {

  def listUsers = Action {
    Ok(Json.toJson(users))
  }

}
