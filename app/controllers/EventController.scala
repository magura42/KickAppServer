package controllers

import java.sql.Date
import javax.inject.Inject

import dao.Role
import dao.TeameventDAO
import dao.TeameventparticipantDAO
import dao.TournamentDAO
import dao.TournamentparticipantDAO
import dao.TrainingDAO
import dao.TrainingparticipantDAO
import models.Event._
import models.EventMaker
import models.TeameventMaker
import models.Teameventparticipant.Teameventparticipant
import models.Tournament.Tournament
import models.TournamentMaker
import models.Tournamentparticipant.Tournamentparticipant
import models.Training.Training
import models.TrainingMaker
import models.Trainingparticipant.Trainingparticipant
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class EventController @Inject()(tournamentDao: TournamentDAO, tournamentparticipantDao: TournamentparticipantDAO,
  trainingDao: TrainingDAO, trainingparticipantDAO: TrainingparticipantDAO,
  teameventDao: TeameventDAO, teameventparticipantDao: TeameventparticipantDAO)
  extends Controller {

  implicit def dateOrdering: Ordering[Date] = new Ordering[Date] {
    def compare(x: Date, y: Date): Int = x compareTo y
  }

  def getTeamEvents(teamId: Int) = Action.apply { implicit request =>

    var events: Seq[Event] = Seq[Event]()


    val trainings: Seq[Training] = Await.result(trainingDao.getTrainings(teamId), Duration.Inf)

    val trournaments: Seq[Tournament] = Await.result(tournamentDao.getTournaments(teamId), Duration.Inf)

    val eTrainings:Seq[Event] = for (e <- trainings) yield EventMaker(e)
    val eTournaments:Seq[Event] = for (e <- trournaments) yield EventMaker(e)
    events = events ++ eTrainings ++ eTournaments

    var sortedEvents: Seq[Event] = events.sortBy(_.date).slice(0,4)

    sortedEvents.foreach(event => {

      event.eventType match {
        case "training" => {
          var participants: Seq[Trainingparticipant] = Await
            .result(trainingparticipantDAO.getPlayers(event.eventId), Duration.Inf)
          event.participationYes ++=
            participants.withFilter(x => x.participantstatus == "yes").map(_.participantid)
          event.participationMaybe ++=
            participants.withFilter(x => x.participantstatus == "maybe").map(_.participantid)
          event.participationNo ++=
            participants.withFilter(x => x.participantstatus == "no").map(_.participantid)
        }
        case "tournament" => {
          var participants: Seq[Tournamentparticipant] = Await
            .result(tournamentparticipantDao.getPlayers(event.eventId), Duration.Inf)
          event.participationYes ++=
            participants.withFilter(x => x.participantstatus == "yes").map(_.participantid)
          event.participationMaybe ++=
            participants.withFilter(x => x.participantstatus == "maybe").map(_.participantid)
          event.participationNo ++=
            participants.withFilter(x => x.participantstatus == "no").map(_.participantid)
        }
      }
    })

    Ok(Json.toJson(sortedEvents))
  }

  def updateEvent(eventId: Int) = Action.async(parse.json[Event]) { implicit request => {

    val event: Event = request.body
    event.eventType match {
      case "training" => {
        Await.result(trainingparticipantDAO.deleteTrainingparticipants(event.eventId), Duration.Inf);

        event.participationYes.foreach {
          id => {
            Await.result(trainingparticipantDAO.createTrainingparticipant(new Trainingparticipant(0, id,
              event.eventId, Role.player, "yes")), Duration.Inf);
          }
        }

        event.participationNo.foreach {
          id => {
            Await.result(trainingparticipantDAO.createTrainingparticipant(new Trainingparticipant(0, id,
              event.eventId, Role.player, "no")), Duration.Inf);
          }
        }

        event.participationMaybe.foreach {
          id => {
            Await.result(trainingparticipantDAO.createTrainingparticipant(new Trainingparticipant(0, id,
              event.eventId, Role.player, "maybe")), Duration.Inf);
          }
        }
        val training = TrainingMaker(event)
        val affectedRowsCount: Future[Int] = trainingDao.updateTraining(training.trainingid, training)
        affectedRowsCount map {
          case 1 => Ok
          case 0 => NotFound
          case _ => InternalServerError
        }
      }
      case "teamevent" => {
        Await.result(teameventparticipantDao.deleteTeameventparticipants(event.eventId), Duration.Inf);

        event.participationYes.foreach {
          id => {
            Await.result(teameventparticipantDao.createTeameventparticipant(new Teameventparticipant(0, id,
              event.eventId, Role.player, "yes")), Duration.Inf);
          }
        }

        event.participationNo.foreach {
          id => {
            Await.result(teameventparticipantDao.createTeameventparticipant(new Teameventparticipant(0, id,
              event.eventId, Role.player, "no")), Duration.Inf);
          }
        }

        event.participationMaybe.foreach {
          id => {
            Await.result(teameventparticipantDao.createTeameventparticipant(new Teameventparticipant(0, id,
              event.eventId, Role.player, "maybe")), Duration.Inf);
          }
        }
        val teamevent = TeameventMaker(event)
        val affectedRowsCount: Future[Int] = teameventDao.updateTeamevent(teamevent.teameventid, teamevent)
        affectedRowsCount map {
          case 1 => Ok
          case 0 => NotFound
          case _ => InternalServerError
        }
      }
      case "tournament" => {
        Await.result(tournamentparticipantDao.deleteTournamentparticipants(event.eventId), Duration.Inf);

        event.participationYes.foreach {
          id => {
            Await.result(tournamentparticipantDao.createTournamentparticipant(new Tournamentparticipant(0, id,
              event.eventId, Role.player, "yes")), Duration.Inf);
          }
        }

        event.participationNo.foreach {
          id => {
            Await.result(tournamentparticipantDao.createTournamentparticipant(new Tournamentparticipant(0, id,
              event.eventId, Role.player, "no")), Duration.Inf);
          }
        }

        event.participationMaybe.foreach {
          id => {
            Await.result(tournamentparticipantDao.createTournamentparticipant(new Tournamentparticipant(0, id,
              event.eventId, Role.player, "maybe")), Duration.Inf);
          }
        }
        val tournament: Tournament = TournamentMaker(event)
        val affectedRowsCount: Future[Int] = tournamentDao.updateTournament(tournament.tournamentid, tournament)
        affectedRowsCount map {
          case 1 => Ok
          case 0 => NotFound
          case _ => InternalServerError
        }
      }
    }
  }
  }

}
