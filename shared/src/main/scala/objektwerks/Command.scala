package objektwerks

sealed trait Command

sealed trait License:
  val license: String

final case class Register(emailAddress: String) extends Command
final case class Login(emailAddress: String, pin: String) extends Command

final case class Deactivate(license: String) extends Command with License
final case class Reactivate(license: String) extends Command with License

final case class ListProfiles(license: String) extends Command with License
final case class AddProfile(license: String, profile: Profile) extends Command with License
final case class UpdateProfile(license: String, profile: Profile) extends Command with License