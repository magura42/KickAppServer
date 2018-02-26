package controllers

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.mvc._
import service.DataService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class InitAppController @Inject()(dataService: DataService) extends Controller {

  def initData = Action.async { implicit request =>
    dataService.loadData()
    Future(Ok(Json.obj("msg" -> "Init loading of data")))
  }

  def reloadData = Action.async { implicit request =>
    dataService.cleanUpDatabase()
    dataService.loadData()
    Future(Ok(Json.obj("msg" -> "Init reloading of data")))
  }

  def deleteData() = Action.async { implicit request =>
    dataService.cleanUpDatabase()
    Future(Ok(Json.obj("msg" -> "Init cleanup db")))
  }

}
