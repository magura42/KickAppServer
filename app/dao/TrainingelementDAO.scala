package dao

import javax.inject.Inject

import com.google.inject.Singleton
import models.Trainingelement.Trainingelement
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future

class TrainingelementTable(tag: Tag) extends Table[Trainingelement](tag, "trainingelement") {
  def trainingelementid = column[Int]("trainingelementid", O.PrimaryKey, O.AutoInc)
  def trainingid = column[Int]("trainingid")
  def exerciseid = column[Int]("exerciseid")
  def * = (trainingelementid, trainingid, exerciseid) <> (Trainingelement.tupled, Trainingelement.unapply _)
}


@Singleton()
class TrainingelementDAO @Inject()(@NamedDatabase("kickapp") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val trainingelements = TableQuery[TrainingelementTable]

  def all(): Future[Seq[Trainingelement]] = db.run(trainingelements.result)

  def getTrainingelement(trainingelementId: Int): Future[Option[Trainingelement]] = db.run(trainingelements.filter(_.trainingelementid === trainingelementId).result.headOption)

  def deleteTrainingelement(trainingelementId: Int): Future[Int] = db.run(trainingelements.filter(_.trainingelementid === trainingelementId).delete)

  def createTrainingelement(trainingelement: Trainingelement): Future[Int] = {
    val query = (trainingelements returning trainingelements.map(_.trainingelementid)) += trainingelement
    db.run(query)
  }

  def updateTrainingelement(trainingelementId: Int, trainingelement: Trainingelement): Future[Int] = {
    val trainingelementToUpdate: Trainingelement = trainingelement.copy(trainingelementId)
    db.run(trainingelements.filter(_.trainingelementid === trainingelementId).update(trainingelementToUpdate))
  }

  def getExercises(trainingId: Int): Future[Seq[Trainingelement]] = {
    db.run(trainingelements.filter(_.trainingid === trainingId).result)
  }

  def getTrainings(exerciseId: Int): Future[Seq[Trainingelement]] = {
    db.run(trainingelements.filter(_.exerciseid === exerciseId).result)
  }
}