package controllers

import play.api.libs.json._
import play.api.mvc._
import models.User._
import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import dao.UserDAO

import scala.concurrent.Future

class UserController @Inject()(userDao: UserDAO) extends Controller {

//  def listUsers = Action {
//    Ok(Json.toJson(users))
//  }

  def listUsers = Action.async { implicit request =>

    val users: Future[Seq[User]] = userDao.all()

    users map {
      usersList => Ok(Json.toJson(usersList))
    }
  }
}
