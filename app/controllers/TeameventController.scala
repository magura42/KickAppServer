package controllers

import javax.inject.Inject

import dao.TeameventDAO
import dao.TeameventparticipantDAO
import models.Event.Event
import models.EventMaker
import models.Teamevent.Teamevent
import models.Teameventparticipant.Teameventparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class TeameventController @Inject()(teameventDAO: TeameventDAO, teameventparticipantDAO: TeameventparticipantDAO)
  extends Controller {


  def listTeamevents = Action.async { implicit request =>

    val teamevents: Future[Seq[Teamevent]] = teameventDAO.all()

    teamevents map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTeamevent(teameventId: Int) = Action.async { implicit request =>
    val teamevent: Future[Option[Teamevent]] = teameventDAO.getTeamevent(teameventId)
    teamevent map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateTeamevent(teameventId: Int) = Action.async(parse.json[Teamevent]) { implicit request =>
    val teamevent: Teamevent = request.body
    val affectedRowsCount: Future[Int] = teameventDAO.updateTeamevent(teameventId, teamevent)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createTeamevent = Action.async(parse.json[Teamevent]) { implicit request =>
    val teamevent: Teamevent = request.body
    val teameventId: Future[Int] = teameventDAO.createTeamevent(teamevent)
    teameventId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTeamevent(teameventId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = teameventDAO.deleteTeamevent(teameventId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getTeamevents(teamId: Int) = Action.async { implicit request =>

    val teamevents: Future[Seq[Teamevent]] = teameventDAO.getTeamevents(teamId)
    teamevents map {
      ts => {
        var events: List[Event] = List[Event]()
        ts.foreach(tr => {
          var event = EventMaker(tr)
          var participants: Seq[Teameventparticipant] = Await
            .result(teameventparticipantDAO.getPlayers(event.eventId), Duration.Inf)
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

  def getTrainingEvent(teameventId: Int) = Action.async { implicit request =>
    val teamevent: Future[Option[Teamevent]] = teameventDAO.getTeamevent(teameventId)
    teamevent map {
      case Some(t) => {
        var event: Event = EventMaker(t)
        var participants: Seq[Teameventparticipant] = Await
          .result(teameventparticipantDAO.getPlayers(event.eventId), Duration.Inf)
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

