package dao

import java.sql.{Date, Time}
import javax.inject.Inject

import com.google.inject.Singleton
import models.Tournament.Tournament
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Future

class TournamentTable(tag: Tag) extends Table[Tournament](tag, "tournament") {
  def tournamentid = column[Int]("tournamentid", O.PrimaryKey, O.AutoInc)
  def street = column[String]("street")
  def zipcode = column[String]("zipcode")
  def city = column[String]("city")
  def date = column[Date]("date")
  def begintime = column[Time]("begintime")
  def endtime = column[Time]("endtime")
  def gettogethertime = column[Time]("gettogethertime")
  def contact = column[Option[String]]("contact")
  def email = column[Option[String]]("email")
  def telefon = column[Option[String]]("telefon")
  def web = column[Option[String]]("web")
  def * = (tournamentid, street, zipcode, city, date, begintime, endtime, gettogethertime, contact,
    email, telefon, web) <> (Tournament.tupled, Tournament.unapply _)
}

@Singleton()
class TournamentDAO @Inject()(@NamedDatabase("${play.configuration.getProperty('db.name')}") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val tournaments = TableQuery[TournamentTable]

  def all(): Future[Seq[Tournament]] = db.run(tournaments.result)

  def getTournament(tournamentId: Int): Future[Option[Tournament]] = db.run(tournaments.filter(_.tournamentid === tournamentId).result.headOption)

  def deleteTournament(tournamentId: Int): Future[Int] = db.run(tournaments.filter(_.tournamentid === tournamentId).delete)

  def createTournament(tournament: Tournament): Future[Int] = {
    val query = (tournaments returning tournaments.map(_.tournamentid)) += tournament
    db.run(query)
  }

  def updateTournament(tournamentId: Int, tournament: Tournament): Future[Int] = {
    val tournamentToUpdate: Tournament = tournament.copy(tournamentId)
    db.run(tournaments.filter(_.tournamentid === tournamentId).update(tournamentToUpdate))
  }


}
