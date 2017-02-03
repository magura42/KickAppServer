package dao


import scala.concurrent.Future
import javax.inject.Inject

import com.google.inject.Singleton
import models.Person.Person
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, Tag}
import slick.driver.PostgresDriver.api._

class PersonTable(tag: Tag) extends Table[Person](tag, "person") {
  def id = column[Int]("id")
  def firstname = column[String]("firstname")
  def lasttname = column[String]("lastname")
  def email = column[String]("email")
  def password = column[String]("password")
  def login = column[String]("login")
  def isAdmin = column[Boolean]("isadmin")
  def * = (id, firstname, lasttname, email, password, login, isAdmin) <> (Person.tupled, Person.unapply _)
}

@Singleton()
class PersonDAO @Inject()(@NamedDatabase("kickapp") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val user = TableQuery[PersonTable]

  def all(): Future[Seq[Person]] = db.run(user.result)

  def getPerson(personId: Int): Future[Option[Person]] = db.run(user.filter(_.id === personId).result.headOption)
}
