package dao

import javax.inject.Inject

import com.google.inject.Singleton
import dao.Role.Role
import models.Teameventparticipant.Teameventparticipant
import models.Tournamentparticipant.Tournamentparticipant
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery
import slick.lifted.Tag

import scala.concurrent.Future

class TeameventparticipantTable(tag: Tag) extends Table[Teameventparticipant](tag, "teameventparticipant") {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def teameventparticipantid = column[Int]("teameventparticipantid", O.PrimaryKey, O.AutoInc)
  def participantid = column[Int]("participantid")
  def teameventid = column[Int]("teameventid")
  def role = column[Role]("role")
  def participantstatus = column[String]("participantstatus")
  def * = (teameventparticipantid, participantid, teameventid, role, participantstatus) <> (Teameventparticipant.tupled, Teameventparticipant.unapply _)
}
@Singleton()
class TeameventparticipantDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val teameventparticipants = TableQuery[TeameventparticipantTable]

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def all(): Future[Seq[Teameventparticipant]] = db.run(teameventparticipants.result)

  def getTeameventparticipant(teameventparticipantid: Int): Future[Option[Teameventparticipant]] =
    db.run(teameventparticipants.filter(_.teameventparticipantid === teameventparticipantid).result.headOption)

  def deleteTeameventparticipant(teameventparticipantid: Int): Future[Int] =
    db.run(teameventparticipants.filter(_.teameventparticipantid === teameventparticipantid).delete)

  def createTeameventparticipant(teameventparticipant: Teameventparticipant): Future[Int] = {
    val query = (teameventparticipants returning teameventparticipants.map(_.teameventparticipantid)) += teameventparticipant
    db.run(query)
  }

  def updateTeameventparticipant(teameventparticipantid: Int, teameventparticipant: Teameventparticipant): Future[Int] = {
    val teameventparticipantToUpdate: Teameventparticipant = teameventparticipant.copy(teameventparticipantid)
    db.run(teameventparticipants.filter(_.teameventparticipantid === teameventparticipantid).update(teameventparticipantToUpdate))
  }

  def getCoaches(teameventid: Int): Future[Seq[Teameventparticipant]] = {
    db.run(teameventparticipants.filter(t => (t.teameventid === teameventid && t.role === Role.coach)).result)
  }

  def getPlayers(teameventid: Int): Future[Seq[Teameventparticipant]] = {
    db.run(teameventparticipants.filter(t => (t.teameventid === teameventid && t.role === Role.player)).result)
  }

  def deleteTeameventparticipants(teameventId: Int): Future[Int] =
    db.run(teameventparticipants.filter(_.teameventid === teameventId).delete)
}
