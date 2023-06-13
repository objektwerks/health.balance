package objektwerks

import java.time.Instant
import java.util.UUID

import scala.util.Random

sealed trait Entity:
  val id: Long

final case class Account(id: Long = 0,
                         license: String = newLicense,
                         emailAddress: String = "",
                         pin: String = newPin,
                         activated: Long = Instant.now.getEpochSecond,
                         deactivated: Long = 0) extends Entity:
  def toArray: Array[Any] = Array(id, license, pin, activated, deactivated)

object Account:
  private val specialChars = "~!@#$%^&*-+=<>?/:;".toList
  private val random = new Random

  private def newSpecialChar: Char = specialChars(random.nextInt(specialChars.length))

  /**
   * 26 letters + 10 numbers + 18 special characters = 54 combinations
   * 7 alphanumeric char pin = 54^7 ( 1,338,925,209,984 )
   */
  private def newPin: String =
    Random.shuffle(
      Random
        .alphanumeric
        .take(5)
        .mkString
        .prepended(newSpecialChar)
        .appended(newSpecialChar)
    ).mkString

  private def newLicense: String = UUID.randomUUID.toString

  val empty = Account(
    license = "",
    emailAddress = "",
    pin = "",
    activated = 0,
    deactivated = 0
  )

sealed trait Consumable extends Entity:
  val id: Long = 0
  val consumed: Long = Instant.now.getEpochSecond

final case class Food(kind: String) extends Consumable
final case class Liquid(kind: String) extends Consumable

sealed trait Expendable extends Entity:
  val id: Long = 0
  val duration: Long = 0
  val calories: Int = 0
  val expended: Long = Instant.now.getEpochSecond

final case class Exercise(kind: String) extends Expendable
final case class Sleep() extends Expendable

sealed trait Measurable extends Entity:
  val id: Long = 0
  val measured: Long = Instant.now.getEpochSecond

final case class BloodPressure(systolic: Int, diastolic: Int) extends Measurable
final case class Pulse(beatsPerMinute: Int) extends Measurable
final case class Glucose(level: Int) extends Measurable
final case class Height(value: Int) extends Measurable
final case class Weight(value: Int) extends Measurable

sealed trait Exposable extends Entity:
  val id: Long = 0
  val from: Long = 0
  val to: Long = 1

final case class Sunshine() extends Exposable
final case class FreshAir() extends Exposable

sealed trait Observable extends Entity:
  val id: Long = 0
  val level: Int = 0
  val observed: Long = Instant.now.getEpochSecond

final case class Mood() extends Observable
final case class Stress() extends Observable