package objektwerks

import java.time.{Instant, LocalDate, LocalDateTime, ZoneOffset}
import java.util.UUID

import scala.util.Random
import scalafx.beans.property.ObjectProperty

sealed trait Entity:
  val id: Long

object Entity:
  given profileOrdering: Ordering[Profile] = Ordering.by[Profile, String](p => p.name)
  given edibleOrdering: Ordering[Edible] = Ordering.by[Edible, Long](e => e.ate).reverse
  given drinkableOrdering: Ordering[Drinkable] = Ordering.by[Drinkable, Long](d => d.drank).reverse
  given expendableOrdering: Ordering[Expendable] = Ordering.by[Expendable, Long](e => e.finish).reverse
  given measurableOrdering: Ordering[Measurable] = Ordering.by[Measurable, Long](m => m.measured).reverse

  def epochSecondToInstant(epochSecond: Long): Instant = Instant.ofEpochSecond(epochSecond)
  def epochSecondToLocalDateTime(epochSecond: Long): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneOffset.UTC)
  def epochSecondToLocalDate(epochSecond: Long): LocalDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneOffset.UTC).toLocalDate
  def instantToLocalDateTime(instant: Instant): LocalDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
  def localDateTimeToInstant(localDateTime: LocalDateTime): Instant = Instant.from(localDateTime)

  def listToString(list: List[String]): String = list.mkString(",")
  def stringToList(string: String): List[String] = string.split(",").toList

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
  val createdProperty = ObjectProperty[String](this, "created", Entity.epochSecondToLocalDate(created).toString)
  val profile = this

final case class Edible(id: Long = 0,
                        profileId: Long,
                        kind: String = EdibleKind.Fruit.toString,
                        detail: String = "",
                        organic: Boolean = true,
                        calories: Int = 1,
                        ate: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val detailProperty = ObjectProperty[String](this, "detail", detail)
  val organicProperty = ObjectProperty[Boolean](this, "organic", organic)
  val caloriesProperty = ObjectProperty[String](this, "calories", calories.toString)
  val ateProperty = ObjectProperty[String](this, "ate", Instant.ofEpochSecond(ate).toString)
  val edible = this

enum EdibleKind:
  case Fruit, Vegetable, Grain, Dairy, Meat

object EdibleKind:
  def toList: List[String] = EdibleKind.values.map(_.toString).toList

final case class Drinkable(id: Long = 0,
                           profileId: Long,
                           kind: String = DrinkableKind.Nonalcoholic.toString,
                           detail: String = "",
                           organic: Boolean = true,
                           count: Int = 1,
                           calories: Int = 0,
                           drank: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val detailProperty = ObjectProperty[String](this, "detail", detail)
  val organicProperty = ObjectProperty[Boolean](this, "organic", organic)
  val countProperty = ObjectProperty[Int](this, "count", count)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val drankProperty = ObjectProperty[String](this, "drank", Instant.ofEpochSecond(drank).toString)
  val drinkable = this

enum DrinkableKind:
  case Nonalcoholic, Alcoholic

object DrinkableKind:
  def toList: List[String] = DrinkableKind.values.map(_.toString).toList

final case class Expendable(id: Long = 0,
                            profileId: Long,
                            kind: String = ExpendableKind.Aerobic.toString,
                            detail: String = "",
                            sunshine: Boolean = true,
                            freshair: Boolean = true,
                            calories: Int = 0,
                            start: Long = Instant.now.getEpochSecond,
                            finish: Long = Instant.now.getEpochSecond + 1) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val detailProperty = ObjectProperty[String](this, "detail", detail)
  val sunshineProperty = ObjectProperty[Boolean](this, "sunshine", sunshine)
  val freshairProperty = ObjectProperty[Boolean](this, "freshair", freshair)
  val caloriesProperty = ObjectProperty[Int](this, "calories", calories)
  val startProperty = ObjectProperty[String](this, "start", Instant.ofEpochSecond(start).toString)
  val finishProperty = ObjectProperty[String](this, "finish", Instant.ofEpochSecond(finish).toString)
  val expendable = this

enum ExpendableKind:
  case Aerobic, Anaerobic, Sleep

object ExpendableKind:
  def toList: List[String] = ExpendableKind.values.map(_.toString).toList

final case class Measurable(id: Long = 0,
                            profileId: Long,
                            kind: String = MeasurableKind.Pulse.toString,
                            measurement: Int = 0,
                            unit: String = UnitOfMeasure.bpm.toString,
                            measured: Long = Instant.now.getEpochSecond) extends Entity:
  val kindProperty = ObjectProperty[String](this, "kind", kind)
  val measurementProperty = ObjectProperty[Int](this, "measurement", measurement)
  val measuredProperty = ObjectProperty[String](this, "measured", Instant.ofEpochSecond(measured).toString)
  val measurable = this

enum MeasurableKind:
  case Pulse, Weight, Glucose

object MeasurableKind:
  def toList: List[String] = MeasurableKind.values.map(_.toString).toList

enum UnitOfMeasure:
  case bpm, lb, level

object UnitOfMeasure:
  def toList: List[String] = UnitOfMeasure.values.map(_.toString).toList