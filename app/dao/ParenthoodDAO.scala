package dao

import javax.inject.Inject

import com.google.inject.Singleton
import models.Parenthood.Parenthood
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future

class ParenthhoodTable(tag: Tag) extends Table[Parenthood](tag, "parenthood") {
  def parenthoodid = column[Int]("parenthoodid", O.PrimaryKey, O.AutoInc)
  def parentid = column[Int]("parentid")
  def childid = column[Int]("childid")
  def * = (parenthoodid, parentid, childid) <> (Parenthood.tupled, Parenthood.unapply _)
}

@Singleton()
class ParenthoodDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val parenthoods = TableQuery[ParenthhoodTable]

  def all(): Future[Seq[Parenthood]] = db.run(parenthoods.result)

  def getParenthood(parenthoodId: Int): Future[Option[Parenthood]] = db.run(parenthoods.filter(_.parenthoodid === parenthoodId).result.headOption)

  def deleteParenthood(parenthoodId: Int): Future[Int] = db.run(parenthoods.filter(_.parenthoodid === parenthoodId).delete)

  def createParenthood(parenthood: Parenthood): Future[Int] = {
    val query = (parenthoods returning parenthoods.map(_.parenthoodid)) += parenthood
    db.run(query)
  }

  def deleteAll(): Future[Int] =
    db.run(parenthoods.delete)

  def updateParenthood(parenthoodId: Int, parenthood: Parenthood): Future[Int] = {
    val parenthoodToUpdate: Parenthood = parenthood.copy(parenthoodId)
    db.run(parenthoods.filter(_.parenthoodid === parenthoodId).update(parenthoodToUpdate))
  }

  def getChilds(parentId: Int): Future[Seq[Parenthood]] = {
    db.run(parenthoods.filter(_.parentid === parentId).result)
  }

  def getParents(childId: Int): Future[Seq[Parenthood]] = {
    db.run(parenthoods.filter(_.childid === childId).result)
  }
}
