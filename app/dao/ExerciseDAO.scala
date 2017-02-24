package dao

import javax.inject.Inject

import com.google.inject.Singleton
import dao.Exercisetype.Exercisetype
import models.Exercise.Exercise
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.db.NamedDatabase
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future

object Exercisetype extends Enumeration {
  type Exercisetype = Value
  val shot,warmup,pass,trick,duel,goalkeeper,header,hall,freeplay,feeling  = Value
}


class ExerciseTable(tag: Tag) extends Table[Exercise](tag, "exercise") {

  implicit val exercisetypeMapper = MappedColumnType.base[Exercisetype.Value, String](
    e => e.toString,
    s => Exercisetype.withName(s)
  )

  def exerciseid = column[Int]("exerciseid", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def exercisetype = column[Exercisetype]("exercisetype")
  def setup = column[String]("setup")
  def execution = column[String]("execution")
  def variants = column[Option[String]]("variants")
  def graphic = column[Option[Array[Byte]]]("graphic")
  def note = column[Option[String]]("note")
  def * = (exerciseid, name, exercisetype, setup, execution, variants, graphic, note) <> (Exercise.tupled, Exercise.unapply _)
}

@Singleton()
class ExerciseDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  private val exercises = TableQuery[ExerciseTable]

  def all(): Future[Seq[Exercise]] = db.run(exercises.result)

  def getExercise(exerciseId: Int): Future[Option[Exercise]] = db.run(exercises.filter(_.exerciseid === exerciseId).result.headOption)

  def deleteExercise(exerciseId: Int): Future[Int] = db.run(exercises.filter(_.exerciseid === exerciseId).delete)

  def createExercise(exercise: Exercise): Future[Int] = {
    val query = (exercises returning exercises.map(_.exerciseid)) += exercise
    db.run(query)
  }

  def updateExercise(exerciseId: Int, exercise: Exercise): Future[Int] = {
    val exerciseToUpdate: Exercise = exercise.copy(exerciseId)
    db.run(exercises.filter(_.exerciseid === exerciseId).update(exerciseToUpdate))
  }
}