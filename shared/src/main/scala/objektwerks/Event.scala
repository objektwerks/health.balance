package objektwerks

import java.time.Instant

import scalafx.beans.property.ObjectProperty

sealed trait Event

final case class Authorized(isAuthorized: Boolean) extends Event

final case class Registered(account: Account) extends Event
final case class LoggedIn(account: Account) extends Event

final case class Deactivated(account: Account) extends Event
final case class Reactivated(account: Account) extends Event

object Fault:
  def apply(message: String, throwable: Throwable): Fault = Fault(s"$message ${throwable.getMessage}")

final case class Fault(cause: String, occurred: Long = Instant.now.getEpochSecond) extends Event:
  val causeProperty = ObjectProperty[String](this, "cause", cause)
  val occurredProperty = ObjectProperty[Long](this, "occurred", occurred)