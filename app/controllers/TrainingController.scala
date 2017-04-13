package controllers

import javax.inject.Inject

import dao.TrainingDAO
import dao.TrainingparticipantDAO
import models.Event.Event
import models.EventMaker
import models.Participantstatus
import models.Training._
import models.Trainingparticipant.Trainingparticipant
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.Duration

class TrainingController @Inject()(trainingDao: TrainingDAO, trainingparticipantDao: TrainingparticipantDAO)
  extends Controller {


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

  def getTrainings(teamId: Int) = Action.async { implicit request =>

    val trainings: Future[Seq[Training]] = trainingDao.getTrainings(teamId)
    trainings map {
      ts => {
        var events: List[Event] = List[Event]()
        ts.foreach(tr => {
          var event = EventMaker(tr)
          var participants: Seq[Trainingparticipant] = Await
            .result(trainingparticipantDao.getPlayers(event.eventId), Duration.Inf)
          event.participationYes ++=
            participants.withFilter(x => x.participantstatus == Participantstatus.yes).map(_.participantid)
          event.participationMaybe ++=
            participants.withFilter(x => x.participantstatus == Participantstatus.maybe).map(_.participantid)
          event.participationNo ++=
            participants.withFilter(x => x.participantstatus == Participantstatus.no).map(_.participantid)
          events :+= event
        })
        Ok(Json.toJson(events))
      }
    }
  }

  def getTrainingEvent(trainingId: Int) = Action.async { implicit request =>
    val training: Future[Option[Training]] = trainingDao.getTraining(trainingId)
    training map {
      case Some(t) => {
        var event: Event = EventMaker(t)
        var participants: Seq[Trainingparticipant] = Await
          .result(trainingparticipantDao.getPlayers(event.eventId), Duration.Inf)
        event.participationYes ++=
          participants.withFilter(x => x.participantstatus == Participantstatus.yes).map(_.participantid)
        event.participationMaybe ++=
          participants.withFilter(x => x.participantstatus == Participantstatus.maybe).map(_.participantid)
        event.participationNo ++=
          participants.withFilter(x => x.participantstatus == Participantstatus.no).map(_.participantid)
        Ok(Json.toJson(event))
      }
      case None => NotFound
    }
  }
}

