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
  val licenseProperty = ObjectProperty[String](this, "license", license)
  val emailAddressProperty = ObjectProperty[String](this, "emailAddress", emailAddress)
  val pinProperty = ObjectProperty[String](this, "pin", pin)
  val activatedProperty = ObjectProperty[String](this, "activated", Instant.ofEpochSecond(activated).toString)
  val deactivatedProperty = ObjectProperty[String](this, "deactivated", Instant.ofEpochSecond(deactivated).toString)
  val account = this

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
                         created: Long = Instant.now.getEpochSecond) extends Entity:
  val nameProperty = ObjectProperty[String](this, "name", name)
  val createdProperty = ObjectProperty[String](this, "created", Instant.ofEpochSecond(created).toString)
  val profile = this

final case class Entry(id: Long = 0,
                       profileId: Long = 0,
                       created: Long = Instant.now.getEpochSecond) extends Entity:
  val createdProperty = ObjectProperty[String](this, "created", Instant.ofEpochSecond(created).toString)

final case class Edible(id: Long = 0,
                        entryId: Long,
                        kind: String = "", // Food
                        organic: Boolean = true,
                        calories: Int = 1,
                        ate: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val organicProperty = ObjectProperty[Boolean](this, "organic", organic)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val ateProperty = ObjectProperty[String](this, "ate", Instant.ofEpochSecond(ate).toString)

final case class Drinkable(id: Long = 0,
                           entryId: Long,
                           kind: String = "", // Liquid
                           organic: Boolean = true,
                           calories: Int = 0,
                           drank: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val organicProperty = ObjectProperty[Boolean](this, "organic", organic)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val drankProperty = ObjectProperty[String](this, "drank", Instant.ofEpochSecond(drank).toString)

final case class Expendable(id: Long = 0,
                            entryId: Long,
                            kind: String = "", // Exercise, Sleep
                            sunshine: Boolean = true,
                            freshair: Boolean = true,
                            calories: Int = 1,
                            start: Long = Instant.now.getEpochSecond,
                            finish: Long = Instant.now.getEpochSecond + 1) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val sunshineProperty = ObjectProperty[Boolean](this, "sunshine", sunshine)
  val freshairProperty = ObjectProperty[Boolean](this, "freshair", freshair)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val startProperty = ObjectProperty[String](this, "start", Instant.ofEpochSecond(start).toString)
  val finishProperty = ObjectProperty[String](this, "finish", Instant.ofEpochSecond(finish).toString)

final case class Measurable(id: Long = 0,
                            entryId: Long,
                            kind: String = "", // Pulse, Glucose, Height, Weight
                            measurement: Int = 1,
                            measured: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val measurementProperty = ObjectProperty[Int](this, "measurement", measurement)
  val measuredProperty = ObjectProperty[String](this, "measured", Instant.ofEpochSecond(measured).toString)