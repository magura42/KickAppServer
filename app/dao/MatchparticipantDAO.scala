package dao

import javax.inject.Inject

import com.google.inject.Singleton
import dao.Role.Role
import models.Matchparticipant.Matchparticipant
import models.Teameventparticipant.Teameventparticipant
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery
import slick.lifted.Tag

import scala.concurrent.Future

class MatchparticipantTable(tag: Tag) extends Table[Matchparticipant](tag, "matchparticipant") {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def matchparticipantid = column[Int]("matchparticipantid", O.PrimaryKey, O.AutoInc)
  def participantid = column[Int]("participantid")
  def matchid = column[Int]("matchid")
  def role = column[Role]("role")
  def participantstatus = column[String]("participantstatus")
  def * = (matchparticipantid, participantid, matchid, role, participantstatus) <> (Matchparticipant.tupled, Matchparticipant.unapply _)
}

@Singleton()
class MatchparticipantDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val matchparticipants = TableQuery[MatchparticipantTable]

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def all(): Future[Seq[Matchparticipant]] = db.run(matchparticipants.result)

  def getMatchparticipant(matchparticipantid: Int): Future[Option[Matchparticipant]] =
    db.run(matchparticipants.filter(_.matchparticipantid === matchparticipantid).result.headOption)

  def deleteMatchparticipant(matchparticipantid: Int): Future[Int] =
    db.run(matchparticipants.filter(_.matchparticipantid === matchparticipantid).delete)

  def createMatchparticipant(matchparticipant: Matchparticipant): Future[Int] = {
    val query = (matchparticipants returning matchparticipants.map(_.matchparticipantid)) += matchparticipant
    db.run(query)
  }

  def updateMatchparticipant(matchparticipantid: Int, matchparticipant: Matchparticipant): Future[Int] = {
    val matchparticipantToUpdate: Matchparticipant = matchparticipant.copy(matchparticipantid)
    db.run(matchparticipants.filter(_.matchparticipantid === matchparticipantid).update(matchparticipantToUpdate))
  }

  def getCoaches(matchid: Int): Future[Seq[Matchparticipant]] = {
    db.run(matchparticipants.filter(t => (t.matchid === matchid && t.role === Role.coach)).result)
  }

  def getPlayers(matchid: Int): Future[Seq[Matchparticipant]] = {
    db.run(matchparticipants.filter(t => (t.matchid === matchid && t.role === Role.player)).result)
  }

  def deleteMatchparticipants(matchid: Int): Future[Int] =
    db.run(matchparticipants.filter(_.matchid === matchid).delete)
}
