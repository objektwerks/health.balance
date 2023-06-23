package objektwerks

import java.time.Instant

import scalafx.beans.property.ObjectProperty

sealed trait Event

final case class Authorized(isAuthorized: Boolean) extends Event

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
  def apply(message: String, throwable: Throwable): Fault = Fault(s"$message ${throwable.getMessage}")

final case class Fault(cause: String, occurred: Long = Instant.now.getEpochSecond) extends Event:
  val causeProperty = ObjectProperty[String](this, "cause", cause)
  val occurredProperty = ObjectProperty[Long](this, "occurred", occurred)

final case class FaultAdded(id: Long) extends Event