package controllers

import javax.inject.Inject

import dao.TeameventparticipantDAO
import models.Teameventparticipant.Teameventparticipant
import models.Tournamentparticipant.Tournamentparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeameventparticipantController @Inject()(teameventparticipantDAO: TeameventparticipantDAO) extends Controller {

  def listTeameventparticipants = Action.async { implicit request =>

    val teameventparticipants: Future[Seq[Teameventparticipant]] = teameventparticipantDAO.all()

    teameventparticipants map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTeameventparticipant(teameventparticipantId: Int) = Action.async { implicit request =>
    val person: Future[Option[Teameventparticipant]] = teameventparticipantDAO
      .getTeameventparticipant(teameventparticipantId)
    person map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTeameventparticipant(teameventparticipantId: Int) = Action
    .async(parse.json[Teameventparticipant]) { implicit request =>
      val person: Teameventparticipant = request.body
      val affectedRowsCount: Future[Int] = teameventparticipantDAO
        .updateTeameventparticipant(teameventparticipantId, person)
      affectedRowsCount map {
        case 1 => Ok
        case 0 => NotFound
        case _ => InternalServerError
      }
    }


  def createTeameventparticipant = Action.async(parse.json[Teameventparticipant]) { implicit request =>
    val person: Teameventparticipant = request.body
    val personId: Future[Int] = teameventparticipantDAO.createTeameventparticipant(person)
    personId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTeameventparticipant(tournamentparticipantId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = teameventparticipantDAO.deleteTeameventparticipant(tournamentparticipantId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getCoaches(teameventtId: Int) = Action.async { implicit request =>
    val coaches: Future[Seq[Teameventparticipant]] = teameventparticipantDAO.getCoaches(teameventtId)
    coaches map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.participantid)))
      }
    }
  }

  def getPlayers(teameventtId: Int) = Action.async { implicit request =>
    val players: Future[Seq[Teameventparticipant]] = teameventparticipantDAO.getPlayers(teameventtId)
    players map {
      p => {
        Ok(Json.toJson(p.map(_.participantid)))
      }
    }
  }
}
