package objektwerks

import java.time.LocalDate
import java.util.UUID

import scala.util.Random

sealed trait Entity:
  val id: Long

final case class Account(id: Long = 0,
                         license: String = newLicense,
                         emailAddress: String = "",
                         pin: String = newPin,
                         activated: Long = LocalDate.now.toEpochDay,
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
  val id: Long = 0L

final case class Food(kind: String) extends Consumable
final case class Liquid(kind: String) extends Consumable
final case class Sunshine(duration: Long) extends Consumable
final case class FreshAir(duration: Long) extends Consumable

sealed trait Expendable extends Entity:
  val id: Long = 0L

final case class Exercise(kind: String, duration: Long) extends Expendable
final case class Sleep(duration: Long) extends Expendable

sealed trait Measurable extends Entity

sealed trait Observable extends Entity