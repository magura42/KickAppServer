package dao


import java.sql.Date
import javax.inject.Inject

import com.google.inject.Singleton
import dao.Role.Role
import models.Person.Person
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery
import slick.lifted.Tag

import scala.concurrent.Future

object Role extends Enumeration {
  type Role = Value
  val player = Value("player")
  val coach = Value("coach")
  val parent = Value("parent")
}

class PersonTable(tag: Tag) extends Table[Person](tag, "person") {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  def personid = column[Int]("personid", O.PrimaryKey, O.AutoInc)

  def foto = column[Option[String]]("foto")

  def firstname = column[String]("firstname")

  def lastname = column[String]("lastname")

  def street = column[String]("street")

  def zipcode = column[String]("zipcode")

  def city = column[String]("city")

  def telephone = column[Option[String]]("telephone")

  def email = column[Option[String]]("email")

  def birthday = column[Option[Date]]("birthday")

  def login = column[String]("login")

  def password = column[String]("password")

  def role = column[Role]("role")

  def teamid = column[Int]("teamid")

  def passnumber = column[Option[Int]]("passnumber")

  def * = (personid, foto, firstname, lastname, street, zipcode, city, telephone, email,
    birthday, login, password, role, teamid, passnumber) <> (Person.tupled, Person.unapply _)
}

@Singleton()
class PersonDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  private val persons = TableQuery[PersonTable]

  def all(): Future[Seq[Person]] = db.run(persons.result)

  def getPerson(personId: Int): Future[Option[Person]] = db
    .run(persons.filter(_.personid === personId).result.headOption)

  def deletePerson(personId: Int): Future[Int] = db.run(persons.filter(_.personid === personId).delete)

  def createPerson(person: Person): Future[Int] = {
    val query = (persons returning persons.map(_.personid)) += person
    db.run(query)
  }

  def deleteAll(): Future[Int] =
    db.run(persons.delete)

  def updatePerson(personId: Int, person: Person): Future[Int] = {
    val personToUpdate: Person = person.copy(personId)
    db.run(persons.filter(_.personid === personId).update(personToUpdate))
  }

  def getCoaches(teamId: Int): Future[Seq[Person]] = {
    db.run(persons.filter(t => {
      t.teamid === teamId && t.role === Role.coach
    }).result)
  }

  def getPlayers(teamId: Int): Future[Seq[Person]] = {
    db.run(persons.filter(t => {
      t.teamid === teamId && t.role === Role.player
    }).result)
  }
}
