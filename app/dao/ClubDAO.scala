package dao

import java.sql.Blob
import javax.inject.Inject

import com.google.inject.Singleton
import models.Club.Club
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future

class ClubTable(tag: Tag) extends Table[Club](tag, "club") {
  def clubid = column[Int]("clubid", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def street = column[String]("street")
  def zipcode = column[String]("zipcode")
  def city = column[String]("city")
  def logo = column[Array[Byte]]("logo")
  def contact = column[String]("contact")
  def email = column[String]("email")
  def telefon = column[String]("telefon")
  def web = column[String]("web")
  def * = (clubid, name, street, zipcode, city, logo, contact, email, telefon, web) <> (Club.tupled, Club.unapply _)
}

@Singleton()
class ClubDAO @Inject()(@NamedDatabase("kickapp") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val clubs = TableQuery[ClubTable]

  def all(): Future[Seq[Club]] = db.run(clubs.result)

  def getClub(clubId: Int): Future[Option[Club]] = db.run(clubs.filter(_.clubid === clubId).result.headOption)

  def deleteClub(clubId: Int): Future[Int] = db.run(clubs.filter(_.clubid === clubId).delete)

  def createClub(club: Club): Future[Int] = {
    val query = (clubs returning clubs.map(_.clubid)) += club
    db.run(query)
  }

  def updateClub(clubId: Int, club: Club): Future[Int] = {
    val clubToUpdate: Club = club.copy(clubId)
    db.run(clubs.filter(_.clubid === clubId).update(clubToUpdate))
  }
}
