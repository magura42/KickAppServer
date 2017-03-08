package controllers

import javax.inject.Inject

import dao.LoginSessionDAO
import models.Authentication.Authentication
import models.LoginSession.LoginSession
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoginSessionController @Inject()(loginSessionDAO: LoginSessionDAO) extends Controller {


  def login() = Action.async(parse.json[Authentication]) { implicit request =>

    val authentication: Authentication = request.body

    val loginSession: Future[Option[LoginSession]] = loginSessionDAO.login(authentication.username, authentication.password)
    loginSession map {
      case Some(ls) => Ok(Json.toJson(ls))
      case None => NotFound
    }
  }

  def logout() = Action { implicit request =>
    //TODO noch einiges zu tun hier...
    Ok("")
  }
}