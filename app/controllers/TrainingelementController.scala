package controllers

import javax.inject.Inject

import dao.TrainingelementDAO
import models.Trainingelement._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TrainingelementController @Inject()(trainingelementDao: TrainingelementDAO) extends Controller {

  def listTrainingelement = Action.async { implicit request =>

    val trainingelements: Future[Seq[Trainingelement]] = trainingelementDao.all()

    trainingelements map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTrainingelement(trainingelementId: Int) = Action.async { implicit request =>
    val trainingelement: Future[Option[Trainingelement]] = trainingelementDao.getTrainingelement(trainingelementId)
    trainingelement map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTrainingelement(trainingelementId: Int) = Action.async(parse.json[Trainingelement]) { implicit request =>
    val trainingelement: Trainingelement = request.body
    val affectedRowsCount: Future[Int] = trainingelementDao.updateTrainingelement(trainingelementId, trainingelement)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createTrainingelement = Action.async(parse.json[Trainingelement]) { implicit request =>
    val trainingelement: Trainingelement = request.body
    val trainingelementId: Future[Int] = trainingelementDao.createTrainingelement(trainingelement)
    trainingelementId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTrainingelement(trainingelementId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = trainingelementDao.deleteTrainingelement(trainingelementId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getExercises(trainingId: Int) = Action.async { implicit request =>
    val trainingelements: Future[Seq[Trainingelement]] = trainingelementDao.getExercises(trainingId)
    trainingelements map {
      p => {
        Ok(Json.toJson(p.map("/exercise/" + _.exerciseid)))
      }
    }
  }

  def getTrainings(exerciseId: Int) = Action.async { implicit request =>
    val trainingelements: Future[Seq[Trainingelement]] = trainingelementDao.getTrainings(exerciseId)
    trainingelements map {
      p => {
        Ok(Json.toJson(p.map("/training/" + _.trainingid)))
      }
    }
  }

}
