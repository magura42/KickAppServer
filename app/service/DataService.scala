package service

import java.sql.{Date, Time}
import java.time.{LocalDate, LocalTime}
import javax.inject.{Inject, Singleton}

import dao.Role.Role
import dao._
import models.Club.Club
import models.Exercise.Exercise
import models.Parenthood.Parenthood
import models.Person.Person
import models.Team.Team
import models.Training.Training
import models.Trainingparticipant.Trainingparticipant
import play.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class DataService @Inject()(clubDao: ClubDAO, teamDao: TeamDAO, personDao: PersonDAO, exerciseDAO: ExerciseDAO,
                            trainingDao: TrainingDAO, trainingparticipantDAO: TrainingparticipantDAO,
                            parenthoodDAO: ParenthoodDAO) {

  clubDao.all() map {
    s => {
      if (s.length == 0) {
        Logger.info("Init the data...")
        loadData()
      } else {
        Logger.info("Data already initialized!")
      }
    }
  }

  private def loadData() = {
    // club
    Logger.info("Load club...");
    val club = Club(1, "SV Lochhausen", "Bienenheimstraße 7", "81249", "München",
      None, None, None, None, Some("http://www.sv-lochhausen.de"))
    val clubId = Await.result(clubDao.createClub(club), Duration.Inf)

    // team
    Logger.info("Load team...");
    val team = Team(1, "G1", 2011, 2012, None, clubId)
    val teamId = Await.result(teamDao.createTeam(team), Duration.Inf)

    // player
    Logger.info("Load persons...");
    val coachMHarrer = Person(1, "Manfred", "Harrer", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("manni.harrer@gmx.de"), Some(getDate(1974, 10, 28)), "mharrer", "mharrer",
      Role.coach, Some(teamId), None, Some(teamId))
    val playerPHarrer = Person(2, "Peter", "Harrer", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), Some(getDate(2012, 10, 7)), "pharrer", "pharrer",
      Role.player, Some(teamId), None, None)
    val playerJRiechers = Person(3, "Jonas", "Riechers", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), None, "jriechers", "jriechers",
      Role.player, Some(teamId), None, None)
    val parentAHarrer = Person(4, "Andrea", "Harrer", "Toni-Berger-Straße 15", "81249", "München",
      Some("01577/2946631"), Some("andreaharrer@gmx.de"), Some(getDate(1977, 10, 24)), "aharrer", "aharrer",
      Role.parent, Some(teamId), None, None)
    val parentJRiechers = Person(5, "Jan", "Riechers", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), None, "jriechers2", "jriechers2",
      Role.player, Some(teamId), None, None)

    val coachMHarrerId = Await.result(personDao.createPerson(coachMHarrer), Duration.Inf)
    val playerPHarrerId = Await.result(personDao.createPerson(playerPHarrer), Duration.Inf)
    val playerJRiechersId = Await.result(personDao.createPerson(playerJRiechers), Duration.Inf)
    val parentAHarrerId = Await.result(personDao.createPerson(parentAHarrer), Duration.Inf)
    val parentJRiechersId = Await.result(personDao.createPerson(parentJRiechers), Duration.Inf)

    // exercises:
    Logger.info("Load exercises...");
    val exercise1 = Exercise(1, "Übergabe Kreis", Exercisetype.warmup,
      "6-10 Spieler bilden einen Kreis und ein Spieler hat einen Ball",
      "Der Spieler läuft mit dem Ball zu einem anderen Spieler und übergibt den Ball (=> Positionswechsel).",
      Some("Steigern mit mehreren Bällen."), None, None)
    val exercise1Id = Await.result(exerciseDAO.createExercise(exercise1), Duration.Inf)

    // training
    Logger.info("Load training...");
    val training1 = Training(1, "Bienenheimstr. 7", "81249", "München", getDate(2017, 9, 11), getTime(16,0),
      getTime(17,0), getTime(15,55))
    val training1Id = Await.result(trainingDao.createTraining(training1), Duration.Inf)

    val training2 = Training(2, "Bienenheimstr. 7", "81249", "München", getDate(2017, 9, 18), getTime(16,0),
      getTime(17,0), getTime(15,55))
    val training2Id = Await.result(trainingDao.createTraining(training2), Duration.Inf)

    val training3 = Training(3, "Bienenheimstr. 7", "81249", "München", getDate(2017, 9, 25), getTime(16,0),
      getTime(17,0), getTime(15,55))
    val training3Id = Await.result(trainingDao.createTraining(training3), Duration.Inf)

    // trainingparticipants
    Logger.info("Load participants...");
    addParticipant(coachMHarrerId, training1Id, Role.coach)
    addParticipant(playerPHarrerId, training1Id, Role.player)
    addParticipant(playerJRiechersId, training1Id, Role.player)
    addParticipant(coachMHarrerId, training2Id, Role.coach)
    addParticipant(playerPHarrerId, training2Id, Role.player)
    addParticipant(playerJRiechersId, training2Id, Role.player)
    addParticipant(coachMHarrerId, training3Id, Role.coach)
    addParticipant(playerPHarrerId, training3Id, Role.player)
    addParticipant(playerJRiechersId, training3Id, Role.player)

    // parenthood:
    Logger.info("Load parenthoods...");
    addParenthood(parentAHarrerId, playerPHarrerId)
    addParenthood(parentJRiechersId, playerJRiechersId)
  }

  private def addParenthood(parentId: Int, playerId: Int) = {
      val p = Parenthood(1, parentId, playerId)
  }

  private def addParticipant(participantId: Int, trainingId: Int, role: Role) = {
    val p = Trainingparticipant(1, participantId, trainingId, role)
    trainingparticipantDAO.createTrainingparticipant(p)
  }

  private def getTime(hour: Int, min: Int): Time = {
    Time.valueOf(LocalTime.of(hour, min))
  }

  private def getDate(year: Int, month: Int, day: Int): Date = {
    Date.valueOf(LocalDate.of(year, month, day))
  }
}