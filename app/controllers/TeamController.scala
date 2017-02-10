package controllers

import javax.inject.Inject

import dao.TeamDAO
import models.Team._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TeamController @Inject()(teamDao: TeamDAO) extends Controller {

  def getTeams(clubId: Option[Int]) = Action.async { implicit request =>
    val teams: Future[Seq[Team]] = clubId match {
      case Some(x) => teamDao.getTeamByClub(x)
      case None => teamDao.all()
    }

    teams map {
      p => Ok(Json.toJson(p))
    }
  }

  def getTeam(teamId: Int) = Action.async { implicit request =>
    val team: Future[Option[Team]] = teamDao.getTeam(teamId)
    team map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def getTeamByClub(clubId: Int) = Action.async { implicit request =>
    val teams: Future[Seq[Team]] = teamDao.getTeamByClub(clubId)
    teams map {
      p => Ok(Json.toJson(p))
    }
  }

  def updateTeam(teamId: Int) = Action.async(parse.json[Team]) { implicit request =>
    val team: Team = request.body
    val affectedRowsCount: Future[Int] = teamDao.updateTeam(teamId, team)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createTeam = Action.async(parse.json[Team]) { implicit request =>
    val team: Team = request.body
    val teamId: Future[Int] = teamDao.createTeam(team)
    teamId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteTeam(teamId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = teamDao.deleteTeam(teamId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

}
