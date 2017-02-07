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

  def updatePerson(personId: Int) = Action.async(parse.json[Person]) { implicit request =>
    val person: Person = request.body
    val affectedRowsCount: Future[Int] = personDao.updatePerson(personId, person)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createPerson = Action.async(parse.json[Person]) { implicit request =>
    val person: Person = request.body
    val personId: Future[Int] = personDao.createPerson(person)
    personId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deletePerson(personId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = personDao.deletePerson(personId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

}
