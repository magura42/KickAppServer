package dao

import javax.inject.Inject

import com.google.inject.Singleton
import dao.Role.Role
import models.Tournamentparticipant.Tournamentparticipant
import models.Trainingparticipant.Trainingparticipant
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery
import slick.lifted.Tag

import scala.concurrent.Future

class TournamentparticipantTable(tag: Tag) extends Table[Tournamentparticipant](tag, "tournamentparticipant") {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def tournamentparticipantid = column[Int]("tournamentparticipantid", O.PrimaryKey, O.AutoInc)
  def participantid = column[Int]("participantid")
  def tournamentid = column[Int]("tournamentid")
  def role = column[Role]("role")
  def participantstatus = column[String]("participantstatus")
  def * = (tournamentparticipantid, participantid, tournamentid, role, participantstatus) <> (Tournamentparticipant.tupled, Tournamentparticipant.unapply _)
}
@Singleton()
class TournamentparticipantDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val tournamentparticipants = TableQuery[TournamentparticipantTable]

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def all(): Future[Seq[Tournamentparticipant]] = db.run(tournamentparticipants.result)

  def getTournamentparticipant(tournamentparticipantid: Int): Future[Option[Tournamentparticipant]] =
    db.run(tournamentparticipants.filter(_.tournamentparticipantid === tournamentparticipantid).result.headOption)

  def deleteTournamentparticipant(tournamentparticipantid: Int): Future[Int] =
    db.run(tournamentparticipants.filter(_.tournamentparticipantid === tournamentparticipantid).delete)

  def createTournamentparticipant(tournamentparticipant: Tournamentparticipant): Future[Int] = {
    val query = (tournamentparticipants returning tournamentparticipants.map(_.tournamentparticipantid)) += tournamentparticipant
    db.run(query)
  }

  def updateTournamentparticipant(tournamentparticipantid: Int, tournamentparticipant: Tournamentparticipant): Future[Int] = {
    val tournamentparticipantToUpdate: Tournamentparticipant = tournamentparticipant.copy(tournamentparticipantid)
    db.run(tournamentparticipants.filter(_.tournamentparticipantid === tournamentparticipantid).update(tournamentparticipantToUpdate))
  }

  def getCoaches(tournamentparticipantid: Int): Future[Seq[Tournamentparticipant]] = {
    db.run(tournamentparticipants.filter(t => (t.tournamentparticipantid === tournamentparticipantid && t.role === Role.coach)).result)
  }

  def getPlayers(tournamentparticipantid: Int): Future[Seq[Tournamentparticipant]] = {
    db.run(tournamentparticipants.filter(t => (t.tournamentparticipantid === tournamentparticipantid && t.role === Role.player)).result)
  }

}
