package objektwerks

import java.time.{Instant, LocalDate}

import scalafx.beans.property.ObjectProperty

sealed trait Event

final case class Registered(account: Account) extends Event
final case class LoggedIn(account: Account) extends Event

final case class Deactivated(account: Account) extends Event
final case class Reactivated(account: Account) extends Event

final case class ProfilesListed(profiles: List[Profile]) extends Event
final case class ProfileAdded(id: Long) extends Event
final case class ProfileUpdated(id: Long) extends Event

final case class EdiblesListed(edibles: List[Edible]) extends Event
final case class EdibleAdded(id: Long) extends Event
final case class EdibleUpdated(id: Long) extends Event

final case class DrinkablesListed(drinkables: List[Drinkable]) extends Event
final case class DrinkableAdded(id: Long) extends Event
final case class DrinkableUpdated(id: Long) extends Event

final case class ExpendablesListed(expendables: List[Expendable]) extends Event
final case class ExpendableAdded(id: Long) extends Event
final case class ExpendableUpdated(id: Long) extends Event

final case class MeasurablesListed(measurables: List[Measurable]) extends Event
final case class MeasurableAdded(id: Long) extends Event
final case class MeasurableUpdated(id: Long) extends Event

object Fault:
  def apply(throwable: Throwable, defaultMessage: String): Fault =
    val message = throwable.getMessage
    Fault(
      if message == null then defaultMessage
      else message
    )

  def apply(prefixMessage: String, throwable: Throwable): Fault =
    val message = throwable.getMessage
    Fault(
      if message == null then prefixMessage
      else s"$prefixMessage $message"
    )

  given faultOrdering: Ordering[Fault] = Ordering.by[Fault, Long](f => LocalDate.parse(f.occurred).toEpochDay()).reverse

final case class Fault (cause: String, occurred: String = Instant.now.toString) extends Event:
  val causeProperty = ObjectProperty[String](this, "cause", cause)
  val occurredProperty = ObjectProperty[String](this, "occurred", occurred)

final case class FaultAdded() extends Event