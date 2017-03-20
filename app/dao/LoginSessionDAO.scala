package dao

import javax.inject.Inject

import com.google.inject.Singleton
import dao.Role.Role
import models.LoginSession.LoginSession
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton()
class LoginSessionDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  implicit val roleMapper = MappedColumnType.base[Role, String](
    e => e.toString,
    s => Role.withName(s)
  )

  private val clubs = TableQuery[ClubTable]
  private val teams = TableQuery[TeamTable]
  private val persons = TableQuery[PersonTable]


  def login(login: String, password: String): Future[Option[LoginSession]] = {

    val innerJoin = for {
      ((p, t), c) <- persons.filter(x => {
        x.login === login && x.password === password
      }) join teams on (_.teamid === _.teamid) join clubs on (_._2.clubid === _.clubid)
    } yield (p.personid, p.firstname, p.lastname, p.role, t.teamid, t.name, c.clubid, c.name)

    db.run(innerJoin.result.headOption map {
      case Some(x) => Option(new LoginSession(x._1, x._2 + ' ' + x._3, x._4, x._5, x._6, x._7, x._8))
      case _ => None
    })
  }

}
