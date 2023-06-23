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

final case class ListEdibles(license: String, profileId: Long) extends Command with License
final case class AddEdible(license: String, edible: Edible) extends Command with License
final case class UpdateEdible(license: String, edible: Edible) extends Command with License

final case class ListDrinkables(license: String, profileId: Long) extends Command with License
final case class AddDrinkable(license: String, drinkable: Drinkable) extends Command with License
final case class UpdateDrinkable(license: String, drinkable: Drinkable) extends Command with License

final case class ListExpendables(license: String, profileId: Long) extends Command with License
final case class AddExpendable(license: String, expendable: Expendable) extends Command with License
final case class UpdateExpendable(license: String, expendable: Expendable) extends Command with License

final case class ListMeasurables(license: String, profileId: Long) extends Command with License
final case class AddMeasurable(license: String, measurable: Measurable) extends Command with License
final case class UpdateMeasurable(license: String, measurable: Measurable) extends Command with License

final case class AddFault(license: String, fault: Fault) extends Command with License