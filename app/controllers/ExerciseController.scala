package controllers

import javax.inject.Inject

import dao.ExerciseDAO
import models.Exercise._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ExerciseController @Inject()(exerciseDao: ExerciseDAO) extends Controller {

  def listExercises = Action.async { implicit request =>

    val exercises: Future[Seq[Exercise]] = exerciseDao.all()

    exercises map {
      p => Ok(Json.toJson(p))
    }
  }

  def getExercise(exerciseId: Int) = Action.async { implicit request =>
    val exercise: Future[Option[Exercise]] = exerciseDao.getExercise(exerciseId)
    exercise map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateExercise(exerciseId: Int) = Action.async(parse.json[Exercise]) { implicit request =>
    val exercise: Exercise = request.body
    val affectedRowsCount: Future[Int] = exerciseDao.updateExercise(exerciseId, exercise)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createExercise = Action.async(parse.json[Exercise]) { implicit request =>
    val exercise: Exercise = request.body
    val exerciseId: Future[Int] = exerciseDao.createExercise(exercise)
    exerciseId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteExercise(exerciseId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = exerciseDao.deleteExercise(exerciseId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }
}
