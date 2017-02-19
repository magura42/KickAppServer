package controllers

import javax.inject.Inject

import dao.TrainingDAO
import models.Training._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TrainingController @Inject()(trainingDao: TrainingDAO) extends Controller {

  def listTraining = Action.async { implicit request =>

    val trainings: Future[Seq[Training]] = trainingDao.all()

    trainings map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTraining(trainingId: Int) = Action.async { implicit request =>
    val training: Future[Option[Training]] = trainingDao.getTraining(trainingId)
    training map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTraining(trainingId: Int) = Action.async(parse.json[Training]) { implicit request =>
    val training: Training = request.body
    val affectedRowsCount: Future[Int] = trainingDao.updateTraining(trainingId, training)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createTraining = Action.async(parse.json[Training]) { implicit request =>
    val training: Training = request.body
    val trainingId: Future[Int] = trainingDao.createTraining(training)
    trainingId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTraining(trainingId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = trainingDao.deleteTraining(trainingId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

}
