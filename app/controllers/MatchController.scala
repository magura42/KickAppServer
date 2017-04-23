package controllers

import javax.inject.Inject

import dao.MatchDAO
import dao.MatchparticipantDAO
import models.Event.Event
import models.EventMaker
import models.Match.Match
import models.Matchparticipant.Matchparticipant
import models.Teameventparticipant.Teameventparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class MatchController @Inject()(matchDao: MatchDAO, matchparticipantDAO: MatchparticipantDAO)
  extends Controller {


  def listMatches = Action.async { implicit request =>

    val matches: Future[Seq[Match]] = matchDao.all()

    matches map {
      p => Ok(Json.toJson(p))
    }
  }

  def getMatch(matchid: Int) = Action.async { implicit request =>
    val sMatch: Future[Option[Match]] = matchDao.getMatch(matchid)
    sMatch map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateMatch(matchid: Int) = Action.async(parse.json[Match]) { implicit request =>
    val sMatch: Match = request.body
    val affectedRowsCount: Future[Int] = matchDao.updateMatch(matchid, sMatch)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createMatch = Action.async(parse.json[Match]) { implicit request =>
    val sMatch: Match = request.body
    val matchid: Future[Int] = matchDao.createMatch(sMatch)
    matchid map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteMatch(matchid: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = matchDao.deleteMatch(matchid)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getMatches(teamId: Int) = Action.async { implicit request =>

    val matches: Future[Seq[Match]] = matchDao.getMatches(teamId)
    matches map {
      ts => {
        var events: List[Event] = List[Event]()
        ts.foreach(tr => {
          var event = EventMaker(tr)
          var participants: Seq[Matchparticipant] = Await
            .result(matchparticipantDAO.getPlayers(event.eventId), Duration.Inf)
          event.participationYes ++=
            participants.withFilter(x => x.participantstatus == "yes").map(_.participantid)
          event.participationMaybe ++=
            participants.withFilter(x => x.participantstatus == "maybe").map(_.participantid)
          event.participationNo ++=
            participants.withFilter(x => x.participantstatus == "no").map(_.participantid)
          events :+= event
        })
        Ok(Json.toJson(events))
      }
    }
  }

  def getMatchEvent(matchid: Int) = Action.async { implicit request =>
    val sMatch: Future[Option[Match]] = matchDao.getMatch(matchid)
    sMatch map {
      case Some(t) => {
        var event: Event = EventMaker(t)
        var participants: Seq[Matchparticipant] = Await
          .result(matchparticipantDAO.getPlayers(event.eventId), Duration.Inf)
        event.participationYes ++=
          participants.withFilter(x => x.participantstatus == "yes").map(_.participantid)
        event.participationMaybe ++=
          participants.withFilter(x => x.participantstatus == "maybe").map(_.participantid)
        event.participationNo ++=
          participants.withFilter(x => x.participantstatus == "no").map(_.participantid)
        Ok(Json.toJson(event))
      }
      case None => NotFound
    }
  }
}

