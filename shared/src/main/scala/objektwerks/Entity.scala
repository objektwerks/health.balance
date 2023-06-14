package objektwerks

import java.time.Instant
import java.util.UUID

import scala.util.Random
import scalafx.beans.property.ObjectProperty

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

final case class Profile(id: Long = 0,
                         accountId: Long,
                         name: String,
                         created: Long = Instant.now.getEpochSecond) extends Entity

final case class Edible(id: Long = 0,
                        profileId: Long,
                        kind: String = "", // Food
                        organic: Boolean = true,
                        calories: Int = 1,
                        ate: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val organicProperty = ObjectProperty[Boolean](this, "organic", organic)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val ateProperty = ObjectProperty[String](this, "ate", Instant.ofEpochSecond(ate).toString)

final case class Drinkable(id: Long = 0,
                           profileId: Long,
                           kind: String = "", // Liquid
                           organic: Boolean = true,
                           volume: Int = 1,
                           unit: String = "",
                           calories: Int = 0,
                           drank: Long = Instant.now.getEpochSecond) extends Entity

final case class Expendable(id: Long = 0,
                            profileId: Long,
                            kind: String = "", // Exercise, Sleep
                            sunshine: Boolean = true,
                            freshair: Boolean = true,
                            calories: Int = 1,
                            from: Long = Instant.now.getEpochSecond,
                            to: Long = Instant.now.getEpochSecond + 1) extends Entity

final case class Measurable(id: Long = 0,
                            profileId: Long,
                            kind: String = "", // Pulse, Glucose, Height, Weight
                            value: Int = 1,
                            unit: String = "",
                            measured: Long = Instant.now.getEpochSecond) extends Entity