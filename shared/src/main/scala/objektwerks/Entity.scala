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

final case class Consumable(id: Long = 0,
                            kind: String = "", // Food, Liquid
                            calories: Int = 0,
                            consumed: Long = Instant.now.getEpochSecond) extends Entity

final case class Expendable(id: Long = 0,
                            kind: String = "", // Exercise, Sleep
                            from: Long = 0,
                            to: Long = 1,
                            calories: Int = 0) extends Entity

sealed trait Measurable extends Entity:
  val id: Long = 0
  val kind: String = "" // Pulse, Glucose, Height, Weight
  val value: Int = 0
  val measured: Long = Instant.now.getEpochSecond

sealed trait Exposable extends Entity:
  val id: Long = 0
  val kind: String = "" // Sunshine, FreshAir
  val from: Long = 0
  val to: Long = 1

sealed trait Observable extends Entity:
  val id: Long = 0
  val kind: String = "" // Mood, Stress
  val level: Int = 0
  val observed: Long = Instant.now.getEpochSecond