package controllers

import javax.inject.Inject

import dao.TournamentDAO
import dao.TrainingparticipantDAO
import models.Event.Event
import models.EventMaker
import models.Participantstatus
import models.Tournament._
import models.Training.Training
import models.Trainingparticipant.Trainingparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class TournamentController @Inject()(tournamentDAO: TournamentDAO,
  trainingparticipantDao: TrainingparticipantDAO) extends Controller {

  def listTournament = Action.async { implicit request =>

    val tournaments: Future[Seq[Tournament]] = tournamentDAO.all()

    tournaments map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTournament(tournamentId: Int) = Action.async { implicit request =>
    val tournament: Future[Option[Tournament]] = tournamentDAO.getTournament(tournamentId)
    tournament map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTournament(tournamentId: Int) = Action.async(parse.json[Tournament]) { implicit request =>
    val tournament: Tournament = request.body
    val affectedRowsCount: Future[Int] = tournamentDAO.updateTournament(tournamentId, tournament)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createTournament = Action.async(parse.json[Tournament]) { implicit request =>
    val tournament: Tournament = request.body
    val tournamentId: Future[Int] = tournamentDAO.createTournament(tournament)
    tournamentId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTournament(tournamentId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = tournamentDAO.deleteTournament(tournamentId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getTourmaments(teamId: Int) = Action.async { implicit request =>

    val tournaments: Future[Seq[Tournament]] = tournamentDAO.getTournaments(teamId)
    tournaments map {
      ts => {
        var events: List[Event] = List[Event]()
        ts.foreach(tr => {
          var event = EventMaker(tr)
          var participants: Seq[Trainingparticipant] = Await.result(trainingparticipantDao.getPlayers(event.eventId), Duration.Inf)
          event.participationYes ++=  participants.withFilter(x => x.participantstatus == Participantstatus.yes).map(_.participantid)
          event.participationMaybe ++=  participants.withFilter(x => x.participantstatus == Participantstatus.maybe).map(_.participantid)
          event.participationNo ++=  participants.withFilter(x => x.participantstatus == Participantstatus.no).map(_.participantid)
          events :+= event
        })
        Ok(Json.toJson(events))
      }
    }
  }
}
