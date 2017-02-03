package controllers

import play.api.libs.json._
import play.api.mvc._
import models.Person._
import javax.inject.Inject

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import dao.PersonDAO

import scala.concurrent.Future

class PersonController @Inject()(personDao: PersonDAO) extends Controller {

  def listPersons = Action.async { implicit request =>

    val persons: Future[Seq[Person]] = personDao.all()

    persons map {
      p => Ok(Json.toJson(p))
    }
  }

  def getPerson(personId: Int) = Action.async { implicit request =>
    val person: Future[Option[Person]] = personDao.getPerson(personId)
    person map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

}
