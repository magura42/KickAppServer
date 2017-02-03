package dao


import scala.concurrent.Future
import javax.inject.Inject

import com.google.inject.Singleton
import models.User.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.lifted.{TableQuery, Tag}
import slick.driver.PostgresDriver.api._

class UserTable(tag: Tag) extends Table[User](tag, "person") {
  def id = column[Int]("id")
  def firstname = column[String]("firstname")
  def lasttname = column[String]("lastname")

  def * = (id, firstname, lasttname) <> (User.tupled, User.unapply _)
}

@Singleton()
class UserDAO @Inject()(@NamedDatabase("kickapp") protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val user = TableQuery[UserTable]

  def all(): Future[Seq[User]] = db.run(user.result)
}
