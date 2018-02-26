package dao

import java.sql.Date
import java.sql.Time
import javax.inject.Inject

import com.google.inject.Singleton
import models.Match.Match
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

class MatchTable(tag: Tag) extends Table[Match](tag, "match") {

  def matchid = column[Int]("matchid", O.PrimaryKey, O.AutoInc)

  def street = column[String]("street")

  def zipcode = column[String]("zipcode")

  def city = column[String]("city")

  def date = column[Date]("date")

  def begintime = column[Time]("begintime")

  def endtime = column[Time]("endtime")

  def gettogethertime = column[Time]("gettogethertime")

  def matchtype = column[String]("matchtype")

  def teamid = column[Int]("teamid")

  def * = (matchid, street, zipcode, city, date, begintime, endtime, gettogethertime, matchtype, teamid) <>
    (Match.tupled, Match.unapply _)
}

@Singleton()
class MatchDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  private val matches = TableQuery[MatchTable]

  def all(): Future[Seq[Match]] = db.run(matches.result)

  def getMatch(matchid: Int): Future[Option[Match]] = db.run(matches.filter(_.matchid === matchid).result.headOption)

  def deleteAll(): Future[Int] =
    db.run(matches.delete)

  def getMatches(teamid: Int): Future[Seq[Match]] = db.run(matches.filter(_.teamid === teamid).result)

  def deleteMatch(matchid: Int): Future[Int] = db.run(matches.filter(_.matchid === matchid).delete)

  def createMatch(sMatch: Match): Future[Int] = {
    val query = (matches returning matches.map(_.matchid)) += sMatch
    db.run(query)
  }

  def updateMatch(matchid: Int, sMatch: Match): Future[Int] = {
    val matchToUpdate: Match = sMatch.copy(matchid)
    db.run(matches.filter(_.matchid === matchid).update(matchToUpdate))
  }


}
