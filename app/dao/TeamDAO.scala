package dao

import javax.inject.Inject

import com.google.inject.Singleton
import models.Team.Team
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future

class TeamTable(tag: Tag) extends Table[Team](tag, "team") {
  def teamid = column[Int]("teamid", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def fromyear = column[Int]("fromyear")
  def toyear = column[Int]("toyear")
  def foto = column[Option[String]]("foto")
  def clubid = column[Int]("clubid")
  def info = column[Option[String]]("info")
  def * = (teamid, name, fromyear, toyear, foto, clubid, info) <> (Team.tupled, Team.unapply _)
}
@Singleton()
class TeamDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val teams = TableQuery[TeamTable]

  def all(): Future[Seq[Team]] = db.run(teams.result)

  def getTeam(teamId: Int): Future[Option[Team]] = db.run(teams.filter(_.teamid === teamId).result.headOption)

  def getTeamByClub(clubId: Int): Future[Seq[Team]] = db.run(teams.filter(_.clubid === clubId).result)

  def deleteTeam(teamId: Int): Future[Int] = db.run(teams.filter(_.teamid === teamId).delete)

  def createTeam(team: Team): Future[Int] = {
    val query = (teams returning teams.map(_.teamid)) += team
    db.run(query)
  }

  def updateTeam(teamId: Int, team: Team): Future[Int] = {
    val teamToUpdate: Team = team.copy(teamId)
    db.run(teams.filter(_.teamid === teamId).update(teamToUpdate))
  }
}
