package service

import java.io.File
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
import models.Teamevent.Teamevent
import models.Training.Training
import models.Trainingparticipant.Trainingparticipant
import org.apache.commons.codec.binary.Base64
import org.apache.commons.io.FileUtils
import play.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

@Singleton
class DataService @Inject()(clubDao: ClubDAO, teamDao: TeamDAO, personDao: PersonDAO, exerciseDAO: ExerciseDAO,
                            trainingDao: TrainingDAO, trainingparticipantDAO: TrainingparticipantDAO,
                            parenthoodDAO: ParenthoodDAO, teameventDAO: TeameventDAO) {

  def cleanUpDatabase() = {
    Logger.info("Cleanup db...")
    trainingparticipantDAO.deleteAll()
    teameventDAO.deleteAll()
    parenthoodDAO.deleteAll()
    trainingDao.deleteAll()
    exerciseDAO.deleteAll()
    personDao.deleteAll()
    teamDao.deleteAll()
    clubDao.deleteAll()
  }

  def loadData() = {
    // club
    Logger.info("Load club...")
    val club = Club(1, "SV Lochhausen", "Bienenheimstraße 7", "81249", "München",
      Some(getImageData("svl.png")), None, None, None, Some("http://www.sv-lochhausen.de"))
    val clubId = Await.result(clubDao.createClub(club), Duration.Inf)

    // team
    Logger.info("Load team...")
    val team = Team(1, "G1", 2011, 2012, None, clubId, None)
    val teamId = Await.result(teamDao.createTeam(team), Duration.Inf)

    // player
    Logger.info("Load persons...")
    val coachMHarrer = Person(1, None, "Manfred", "Harrer", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("manni.harrer@gmx.de"), Some(getDate(1974, 10, 28)), "mharrer", "mharrer",
      Role.coach, teamId, None)
    val playerPHarrer = Person(2, None, "Peter", "Harrer", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), Some(getDate(2012, 10, 7)), "pharrer", "pharrer",
      Role.player, teamId, None)
    val playerJRiechers = Person(3, None, "Jonas", "Riechers", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), None, "jriechers", "jriechers",
      Role.player, teamId, None)
    val parentAHarrer = Person(4, None, "Andrea", "Harrer", "Toni-Berger-Straße 15", "81249", "München",
      Some("01577/2946631"), Some("andreaharrer@gmx.de"), Some(getDate(1977, 10, 24)), "aharrer", "aharrer",
      Role.parent, teamId, None)
    val parentJRiechers = Person(5, None, "Jan", "Riechers", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), None, "jriechers2", "jriechers2",
      Role.player, teamId, None)
    val playerJProbst = Person(6, None, "Jakob", "Probst", "Toni-Berger-Straße 15", "81249", "München",
      Some("0176/10351031"), Some("tne@gmx.li"), None, "jprobst", "jprobst",
      Role.player, teamId, None)


    val coachMHarrerId = Await.result(personDao.createPerson(coachMHarrer), Duration.Inf)
    val playerPHarrerId = Await.result(personDao.createPerson(playerPHarrer), Duration.Inf)
    val playerJRiechersId = Await.result(personDao.createPerson(playerJRiechers), Duration.Inf)
    val playerJProbstId = Await.result(personDao.createPerson(playerJProbst), Duration.Inf)
    val parentAHarrerId = Await.result(personDao.createPerson(parentAHarrer), Duration.Inf)
    val parentJRiechersId = Await.result(personDao.createPerson(parentJRiechers), Duration.Inf)

    // exercises:
    Logger.info("Load exercises...")
    val exercise1 = Exercise(1, "Übergabe Kreis", Exercisetype.warmup, Teamtype.F,
      "6-10 Spieler bilden einen Kreis und ein Spieler hat einen Ball",
      "Der Spieler läuft mit dem Ball zu einem anderen Spieler und übergibt den Ball (=> Positionswechsel).",
      Some("Steigern mit mehreren Bällen."), Some(getImageData("uerbergabe_kreis.png")), None)
    val exercise1Id = Await.result(exerciseDAO.createExercise(exercise1), Duration.Inf)

    val exercise2 = Exercise(2, "4 gegen 2", Exercisetype.freeplay, Teamtype.F,
      "Begrenzte Fläche, 10m Kante. 1 Ball. 6 Spieler",
      "2 Spieler in der Mitte versuchen den Ball zu erobern (=> ein Ballkontakt). Danach Spielerwechsel.",
      Some("Varianten: 5 gegen 1, ein Ballkontakt, nur mit schwachem Fuß"),
      Some(getImageData("4vs2.jpg")), None)
    val exercise2Id = Await.result(exerciseDAO.createExercise(exercise2), Duration.Inf)

    val exercise3 = Exercise(3, "Ritterturnier", Exercisetype.warmup, Teamtype.Bambini,
      "Ein 20 x 15 Meter großes Feld markieren. 1 Leibchen pro Kind. Die Kinder stecken sich ein Leibchen in die Hose.",
      "Sie versuchen jeweils, den Mitspielern die Leibchen abzujagen.",
      Some("Die Kinder halten einen Ball in der Hand."),
      Some(getImageData("ritterturnier.png")), None)
    val exercise3Id = Await.result(exerciseDAO.createExercise(exercise3), Duration.Inf)

    val exercise4 = Exercise(4, "Fangen/Versteinern", Exercisetype.warmup, Teamtype.Bambini,
      "Ein 20 x 15 Meter großes Feld markieren. Ohne Ball.",
      "Der Trainer versucht, die Kinder zu fangen. Die Gefangenen bleiben breitbeinig stehen.Sie können befreit werden, indem ein anderes Kind durch die Beine krabbelt.",
      Some("Gefangene können durch Abklatschen befreit werden. Gefangene können sich selbst befreien, indem sie dreimal in die Höhe springen. Einige Kinder oder Eltern unterstützen den Trainer bei der Jagd."),
      Some(getImageData("ritterturnier.png")), None)
    val exercise4Id = Await.result(exerciseDAO.createExercise(exercise4), Duration.Inf)

    val exercise5 = Exercise(5, "Trainer fangen", Exercisetype.shot, Teamtype.Bambini,
      "Ein 20 x 15 Meter großes Feld markieren. Jeder Spieler einen Ball.",
      "Der Trainer läuft langsam durch das Feld. Die Kinder versuchen, per Schuss aus dem Dribbling die Beine des Trainers zu treffen.",
      Some("Zusätzlich Eltern bestimmen, die ebenfalls durch das Feld laufen. Durch die gespreizten Beine der Erwachsenen schießen."),
      Some(getImageData("trainer_fangen.png")), None)
    val exercise5Id = Await.result(exerciseDAO.createExercise(exercise5), Duration.Inf)

    val exercise6 = Exercise(6, "Spiel mit König", Exercisetype.freeplay, Teamtype.Bambini,
      "Ein 20 x 15 Meter großes Feld markieren. 2 Hütchentore markieren. 2 Teams einteilen. Jedes Team stellt 1 Torhüter",
      "3 gegen 3 auf die Tore mit Torhütern.Die Ballbesitzer dürfen den Trainer jederzeit einbeziehen.Dieser darf jedoch keine Tore erzielen.",
      Some("Der Trainer spielt je eine Halbzeit bei jedem Team. In jedem Team spielt ein Erwachsener mit. 4 gegen 4 ohne Torhüter spielen."),
      Some(getImageData("spiel_mit_koenig.png")), None)
    val exerciseId = Await.result(exerciseDAO.createExercise(exercise6), Duration.Inf)

    val exercise7 = Exercise(7, "Ball klauen", Exercisetype.freeplay, Teamtype.Bambini,
      "Ein 20 x 15 Meter großes Feld markieren. Fänger bestimmen.",
      "Fänger (ohne Ball) müssen versuchen, den anderen Spielern den Ball abzunehmen und aus dem Feld herauszuspielen. Gefangene Spieler bleiben im Grätschschritt stehen. Spielen ihnen die anderen Spieler einen Ball zwischen den Beinen durch, können sie sich einen Ball holen und wieder mitmachen.",
      Some("Gefangene werden zu Fängern, der erfolgreiche Fänger erhält den Ball und wechselt auch die andere Seite."),
      None, None)
    Await.result(exerciseDAO.createExercise(exercise7), Duration.Inf)

    val exercise8 = Exercise(8, "Torschuss-Team-Spiel", Exercisetype.shot, Teamtype.F,
      "Ein 20 x 30 Meter großes Feld markieren. Zwei Tore. In der Mitte vier farbige Hüttchen. Starthüttchen neben den Toren.",
      "Der Trainer ruft eine Hütchenfarbe.\nDie Spieler umdribbeln das Hütchen und schießen auf das Tor neben dem eigenen Starthütchen.\nDie Spieler stellen sich am eigenen Starthütchen wieder an.",
      Some("Die Spieler umlaufen ohne Ball das Hütchen und erhalten einen Pass vom nächsten Mitspieler.\nDer Trainer ruft 2 Hütchen auf."),
      Some(getImageData("team_torschuss.png")), Some("Die Starthütchen in ausreichendem Abstand zum Tor aufstellen, damit die Spieler nicht von Torschüssen getroffen werden können.\nAuf eine enge Ballführung achten."))
    Await.result(exerciseDAO.createExercise(exercise8), Duration.Inf)

    // teamevent
    Logger.info("Load teamevent...")
    val teamevent1 = Teamevent(1, "Sommerfest", "Bienenheimstr. 7", "81249", "München", getDate(2018, 7, 30), getTime(15,0),
      getTime(18,0), getTime(14,55), teamId)
    Await.result(teameventDAO.createTeamevent(teamevent1), Duration.Inf)
    val teamevent2 = Teamevent(2, "Saisonn KickOff", "Bienenheimstr. 7", "81249", "München", getDate(2018, 8, 15), getTime(16,0),
      getTime(17,0), getTime(15,55), teamId)
    Await.result(teameventDAO.createTeamevent(teamevent2), Duration.Inf)


    // training
    Logger.info("Load training...")
    val training1 = Training(1, "Bienenheimstr. 7", "81249", "München", getDate(2018, 9, 11), getTime(16,0),
      getTime(17,0), getTime(15,55), teamId)
    val training1Id = Await.result(trainingDao.createTraining(training1), Duration.Inf)

    val training2 = Training(2, "Bienenheimstr. 7", "81249", "München", getDate(2018, 9, 18), getTime(16,0),
      getTime(17,0), getTime(15,55), teamId)
    val training2Id = Await.result(trainingDao.createTraining(training2), Duration.Inf)

    val training3 = Training(3, "Bienenheimstr. 7", "81249", "München", getDate(2018, 9, 25), getTime(16,0),
      getTime(17,0), getTime(15,55), teamId)
    val training3Id = Await.result(trainingDao.createTraining(training3), Duration.Inf)

    // trainingparticipants
    Logger.info("Load participants...")
    addParticipant(coachMHarrerId, training1Id, Role.coach)
    addParticipant(playerPHarrerId, training1Id, Role.player)
    addParticipant(playerJRiechersId, training1Id, Role.player)
    addParticipant(playerPHarrerId, training1Id, Role.player)
    addParticipant(playerJProbstId, training1Id, Role.player)

    addParticipant(coachMHarrerId, training2Id, Role.coach)
    addParticipant(playerPHarrerId, training2Id, Role.player)
    addParticipant(playerJRiechersId, training2Id, Role.player)
    addParticipant(playerPHarrerId, training2Id, Role.player)
    addParticipant(playerJProbstId, training2Id, Role.player)

    addParticipant(coachMHarrerId, training3Id, Role.coach)
    addParticipant(playerPHarrerId, training3Id, Role.player)
    addParticipant(playerJRiechersId, training3Id, Role.player)
    addParticipant(playerPHarrerId, training3Id, Role.player)
    addParticipant(playerJProbstId, training3Id, Role.player)

    // parenthood:
    Logger.info("Load parenthoods...")
    addParenthood(parentAHarrerId, playerPHarrerId)
    addParenthood(parentJRiechersId, playerJRiechersId)
  }

  private def addParenthood(parentId: Int, playerId: Int) = {
      val p = Parenthood(1, parentId, playerId)
  }

  private def addParticipant(participantId: Int, trainingId: Int, role: Role) = {
    val p = Trainingparticipant(1, participantId, trainingId, role, "yes")
    trainingparticipantDAO.createTrainingparticipant(p)
  }

  private def getTime(hour: Int, min: Int): Time = {
    Time.valueOf(LocalTime.of(hour, min))
  }

  private def getDate(year: Int, month: Int, day: Int): Date = {
    Date.valueOf(LocalDate.of(year, month, day))
  }

  private def getImageData(filename: String): String = {
    val file = new File("./data/images/" + filename)
    "data:image/png;base64," + Base64.encodeBase64String(FileUtils.readFileToByteArray(file))
  }
}