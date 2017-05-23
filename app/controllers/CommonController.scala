package controllers

import javax.inject.Inject

import dao.PersonDAO
import models.Person.Person
import play.api.mvc.Controller

import scala.concurrent.Await
import scala.concurrent.duration.Duration


class CommonController @Inject()(personDao: PersonDAO) extends Controller {

  def getParticipantName(personId: Int): String = {
    var person: Person = Await.result(personDao.getPerson(personId), Duration.Inf).get;
    return person.lastname + ", " + person.firstname;
  }
}
