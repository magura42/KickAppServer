package dao

import java.sql.Date
import javax.inject.Inject

import com.google.inject.Singleton
import models.Trainingparticipant.Trainingparticipant
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery, Tag}
import dao.Role.Role
import scala.concurrent.Future

class TraningparticipantTable(tag: Tag) extends Table[Trainingparticipant](tag, "trainingparticipant") {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def trainingparticipantid = column[Int]("trainingparticipantid", O.PrimaryKey, O.AutoInc)
  def participantid = column[Int]("participantid")
  def trainingid = column[Int]("trainingid")
  def role = column[Role]("role")
  def * = (trainingparticipantid, participantid, trainingid, role) <> (Trainingparticipant.tupled, Trainingparticipant.unapply _)
}


@Singleton()
class TrainingparticipantDAO @Inject()(@NamedDatabase("kickapp") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val trainingparticipants = TableQuery[TraningparticipantTable]

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def all(): Future[Seq[Trainingparticipant]] = db.run(trainingparticipants.result)

  def getTrainingparticipant(trainingparticipantId: Int): Future[Option[Trainingparticipant]] =
    db.run(trainingparticipants.filter(_.trainingparticipantid === trainingparticipantId).result.headOption)

  def deleteTrainingparticipant(trainingparticipantId: Int): Future[Int] =
    db.run(trainingparticipants.filter(_.trainingparticipantid === trainingparticipantId).delete)

  def createTrainingparticipant(trainingparticipant: Trainingparticipant): Future[Int] = {
    val query = (trainingparticipants returning trainingparticipants.map(_.trainingparticipantid)) += trainingparticipant
    db.run(query)
  }

  def updateTrainingparticipant(trainingparticipantId: Int, trainingparticipant: Trainingparticipant): Future[Int] = {
    val trainingparticipantToUpdate: Trainingparticipant = trainingparticipant.copy(trainingparticipantId)
    db.run(trainingparticipants.filter(_.trainingparticipantid === trainingparticipantId).update(trainingparticipantToUpdate))
  }

  def getCoaches(trainingId: Int): Future[Seq[Trainingparticipant]] = {
    db.run(trainingparticipants.filter(t => (t.trainingid === trainingId && t.role === Role.coach)).result)
  }

  def getPlayers(trainingId: Int): Future[Seq[Trainingparticipant]] = {
    db.run(trainingparticipants.filter(t => (t.trainingid === trainingId && t.role === Role.player)).result)
  }

}
