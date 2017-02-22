package controllers

import javax.inject.Inject

import dao.TrainingparticipantDAO
import models.Trainingparticipant._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TrainingparticipantController @Inject()(trainingparticipantDao: TrainingparticipantDAO) extends Controller {

  def listTrainingparticipants = Action.async { implicit request =>

    val trainingparticipants: Future[Seq[Trainingparticipant]] = trainingparticipantDao.all()

    trainingparticipants map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTrainingparticipant(trainingparticipantId: Int) = Action.async { implicit request =>
    val person: Future[Option[Trainingparticipant]] = trainingparticipantDao.getTrainingparticipant(trainingparticipantId)
    person map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTrainingparticipant(trainingparticipantId: Int) = Action.async(parse.json[Trainingparticipant]) { implicit request =>
    val person: Trainingparticipant = request.body
    val affectedRowsCount: Future[Int] = trainingparticipantDao.updateTrainingparticipant(trainingparticipantId, person)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createTrainingparticipant = Action.async(parse.json[Trainingparticipant]) { implicit request =>
    val person: Trainingparticipant = request.body
    val personId: Future[Int] = trainingparticipantDao.createTrainingparticipant(person)
    personId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTrainingparticipant(trainingparticipantId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = trainingparticipantDao.deleteTrainingparticipant(trainingparticipantId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getCoaches(trainingId: Int) = Action.async { implicit request =>
    val coaches: Future[Seq[Trainingparticipant]] = trainingparticipantDao.getCoaches(trainingId)
    coaches map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.participantid)))
      }
    }
  }

  def getPlayers(trainingId: Int) = Action.async { implicit request =>
    val players: Future[Seq[Trainingparticipant]] = trainingparticipantDao.getPlayers(trainingId)
    players map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.participantid)))
      }
    }
  }
}
