package dao

import java.sql.Date
import java.sql.Time
import javax.inject.Inject

import com.google.inject.Singleton
import models.Teamevent.Teamevent
import models.Training.Training
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

class TeameventTable(tag: Tag) extends Table[Teamevent](tag, "teamevent") {

  def teameventid = column[Int]("teameventid", O.PrimaryKey, O.AutoInc)
  def street = column[String]("street")
  def zipcode = column[String]("zipcode")
  def city = column[String]("city")
  def date = column[Date]("date")
  def begintime = column[Time]("begintime")
  def endtime = column[Time]("endtime")
  def gettogethertime = column[Time]("gettogethertime")
  def teamid = column[Int]("teamid")
  def * = (teameventid, street, zipcode, city, date, begintime, endtime, gettogethertime, teamid) <> (Teamevent.tupled, Teamevent.unapply _)
}

@Singleton()
class TeameventDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val teamevents = TableQuery[TeameventTable]

  def all(): Future[Seq[Teamevent]] = db.run(teamevents.result)

  def getTeamevent(teameventId: Int): Future[Option[Teamevent]] = db.run(teamevents.filter(_.teameventid === teameventId).result.headOption)

  def getTeamevents(teamId: Int): Future[Seq[Teamevent]] = db.run(teamevents.filter(_.teamid === teamId).result)

  def deleteTeamevent(teameventId: Int): Future[Int] = db.run(teamevents.filter(_.teameventid === teameventId).delete)

  def createTeamevent(teamevent: Teamevent): Future[Int] = {
    val query = (teamevents returning teamevents.map(_.teameventid)) += teamevent
    db.run(query)
  }

  def updateTeamevent(teameventId: Int, teamevent: Teamevent): Future[Int] = {
    val teameventToUpdate: Teamevent = teamevent.copy(teameventId)
    db.run(teamevents.filter(_.teameventid === teameventId).update(teameventToUpdate))
  }


}
