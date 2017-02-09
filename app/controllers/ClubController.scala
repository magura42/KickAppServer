package controllers

import javax.inject.Inject

import dao.ClubDAO
import models.Club._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ClubController @Inject()(clubDao: ClubDAO) extends Controller {

  def listClubs = Action.async { implicit request =>

    val clubs: Future[Seq[Club]] = clubDao.all()

    clubs map {
      p => Ok(Json.toJson(p))
    }
  }

  def getClub(clubId: Int) = Action.async { implicit request =>
    val club: Future[Option[Club]] = clubDao.getClub(clubId)
    club map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateClub(clubId: Int) = Action.async(parse.json[Club]) { implicit request =>
    val club: Club = request.body
    val affectedRowsCount: Future[Int] = clubDao.updateClub(clubId, club)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createClub = Action.async(parse.json[Club]) { implicit request =>
    val club: Club = request.body
    val clubId: Future[Int] = clubDao.createClub(club)
    clubId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteClub(clubId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = clubDao.deleteClub(clubId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

}
