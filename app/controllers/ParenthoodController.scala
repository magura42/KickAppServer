package controllers

import javax.inject.Inject

import dao.ParenthoodDAO
import models.Parenthood._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ParenthoodController @Inject()(parenthoodDao: ParenthoodDAO) extends Controller {

  def listParenthoods = Action.async { implicit request =>

    val parenthoods: Future[Seq[Parenthood]] = parenthoodDao.all()

    parenthoods map {
      p => Ok(Json.toJson(p))
    }
  }

  def getParenthood(parenthoodId: Int) = Action.async { implicit request =>
    val parenthood: Future[Option[Parenthood]] = parenthoodDao.getParenthood(parenthoodId)
    parenthood map {
      case Some(p) => Ok(Json.toJson(p))
      case None => NotFound
    }
  }

  def updateParenthood(parenthoodId: Int) = Action.async(parse.json[Parenthood]) { implicit request =>
    val parenthood: Parenthood = request.body
    val affectedRowsCount: Future[Int] = parenthoodDao.updateParenthood(parenthoodId, parenthood)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }


  def createParenthood = Action.async(parse.json[Parenthood]) { implicit request =>
    val parenthood: Parenthood = request.body
    val parenthoodId: Future[Int] = parenthoodDao.createParenthood(parenthood)
    parenthoodId map {
      case id => Created(Json.toJson(id))
    }
  }

  def deleteParenthood(parenthoodId: Int) = Action.async { implicit request =>
    val affectedRowsCount: Future[Int] = parenthoodDao.deleteParenthood(parenthoodId)
    affectedRowsCount map {
      case 1 => Ok
      case 0 => NotFound
      case _ => InternalServerError
    }
  }

  def getChilds(parentId: Int) = Action.async { implicit request =>
    val parents: Future[Seq[Parenthood]] = parenthoodDao.getChilds(parentId)
    parents map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.childid)))
      }
    }
  }

  def getParents(childId: Int) = Action.async { implicit request =>
    val childs: Future[Seq[Parenthood]] = parenthoodDao.getParents(childId)
    childs map {
      p => {
        Ok(Json.toJson(p.map("/person/" + _.parentid)))
      }
    }
  }

}
