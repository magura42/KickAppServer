package controllers

import javax.inject.Inject

import dao.TournamentparticipantDAO
import models.Tournamentparticipant.Tournamentparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TournamentparticipantController @Inject()(tournamentparticipantDAO: TournamentparticipantDAO) extends Controller {

  def listTournamentparticipants = Action.async { implicit request =>

    val tournamentparticipants: Future[Seq[Tournamentparticipant]] = tournamentparticipantDAO.all()

    tournamentparticipants map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTournamentparticipant(tournamentparticipantId: Int) = Action.async { implicit request =>
    val person: Future[Option[Tournamentparticipant]] = tournamentparticipantDAO
      .getTournamentparticipant(tournamentparticipantId)
    person map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTournamentparticipant(tournamentparticipantId: Int) = Action
    .async(parse.json[Tournamentparticipant]) { implicit request =>
      val person: Tournamentparticipant = request.body
      val affectedRowsCount: Future[Int] = tournamentparticipantDAO
        .updateTournamentparticipant(tournamentparticipantId, person)
      affectedRowsCount map {
        case 1 => Ok
        case 0 => NotFound
        case _ => InternalServerError
      }
    }


  def createTournamentparticipant = Action.async(parse.json[Tournamentparticipant]) { implicit request =>
    val person: Tournamentparticipant = request.body
    val personId: Future[Int] = tournamentparticipantDAO.createTournamentparticipant(person)
    personId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTournamentparticipant(tournamentparticipantId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = tournamentparticipantDAO.deleteTournamentparticipant(tournamentparticipantId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getCoaches(tournamentId: Int) = Action.async { implicit request =>
    val coaches: Future[Seq[Tournamentparticipant]] = tournamentparticipantDAO.getCoaches(tournamentId)
    coaches map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.participantid)))
      }
    }
  }

  def getPlayers(tournamentId: Int) = Action.async { implicit request =>
    val players: Future[Seq[Tournamentparticipant]] = tournamentparticipantDAO.getPlayers(tournamentId)
    players map {
      p => {
        Ok(Json.toJson(p.map(_.participantid)))
      }
    }
  }
}
