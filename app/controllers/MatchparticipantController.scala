package controllers

import javax.inject.Inject

import dao.MatchparticipantDAO
import models.Matchparticipant.Matchparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class MatchparticipantController @Inject()(matchparticipantDAO: MatchparticipantDAO) extends Controller {

  def listMatchparticipants = Action.async { implicit request =>

    val matchparticipants: Future[Seq[Matchparticipant]] = matchparticipantDAO.all()

    matchparticipants map {
      p => Ok(Json.toJson(p))
    }
  }

  def getMatchparticipant(matchparticipantId: Int) = Action.async { implicit request =>
    val person: Future[Option[Matchparticipant]] = matchparticipantDAO
      .getMatchparticipant(matchparticipantId)
    person map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTeameventparticipant(matchparticipantId: Int) = Action
    .async(parse.json[Matchparticipant]) { implicit request =>
      val person: Matchparticipant = request.body
      val affectedRowsCount: Future[Int] = matchparticipantDAO
        .updateMatchparticipant(matchparticipantId, person)
      affectedRowsCount map {
        case 1 => Ok
        case 0 => NotFound
        case _ => InternalServerError
      }
    }


  def createMatchparticipant = Action.async(parse.json[Matchparticipant]) { implicit request =>
    val person: Matchparticipant = request.body
    val personId: Future[Int] = matchparticipantDAO.createMatchparticipant(person)
    personId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteMatchparticipant(matchparticipantId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = matchparticipantDAO.deleteMatchparticipant(matchparticipantId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getCoaches(matchid: Int) = Action.async { implicit request =>
    val coaches: Future[Seq[Matchparticipant]] = matchparticipantDAO.getCoaches(matchid)
    coaches map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.participantid)))
      }
    }
  }

  def getPlayers(matchid: Int) = Action.async { implicit request =>
    val players: Future[Seq[Matchparticipant]] = matchparticipantDAO.getPlayers(matchid)
    players map {
      p => {
        Ok(Json.toJson(p.map(_.participantid)))
      }
    }
  }
}
