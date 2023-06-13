package objektwerks

sealed trait Command

sealed trait License:
  val license: String

final case class Register(emailAddress: String) extends Command
final case class Login(emailAddress: String, pin: String) extends Command