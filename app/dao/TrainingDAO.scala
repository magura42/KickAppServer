package dao

import java.sql.{Date, Time, Timestamp}
import javax.inject.Inject

import com.google.inject.Singleton
import models.Training.Training
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future



class TrainingTable(tag: Tag) extends Table[Training](tag, "training") {

  def trainingid = column[Int]("trainingid", O.PrimaryKey, O.AutoInc)
  def street = column[String]("street")
  def zipcode = column[String]("zipcode")
  def city = column[String]("city")
  def date = column[Date]("date")
  def begintime = column[Time]("begintime")
  def endtime = column[Time]("endtime")
  def gettogethertime = column[Time]("gettogethertime")
  def teamid = column[Int]("teamid")
  def * = (trainingid, street, zipcode, city, date, begintime, endtime, gettogethertime, teamid) <> (Training.tupled, Training.unapply _)
}

@Singleton()
class TrainingDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val trainings = TableQuery[TrainingTable]

  def all(): Future[Seq[Training]] = db.run(trainings.result)

  def getTraining(trainingId: Int): Future[Option[Training]] = db.run(trainings.filter(_.trainingid === trainingId).result.headOption)

  def getTrainings(teamId: Int): Future[Seq[Training]] = db.run(trainings.filter(_.teamid === teamId).result)

  def deleteTraining(trainingId: Int): Future[Int] = db.run(trainings.filter(_.trainingid === trainingId).delete)

  def createTraining(training: Training): Future[Int] = {
    val query = (trainings returning trainings.map(_.trainingid)) += training
    db.run(query)
  }

  def updateTraining(trainingId: Int, training: Training): Future[Int] = {
    val trainingToUpdate: Training = training.copy(trainingId)
    db.run(trainings.filter(_.trainingid === trainingId).update(trainingToUpdate))
  }


}
