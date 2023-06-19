package objektwerks

import scala.util.Try
import scala.util.control.NonFatal

import Validator.*

final class Dispatcher(store: Store, emailer: Emailer):
  def dispatch[E <: Event](command: Command): Event =
    if !command.isValid then Fault(s"Command is invalid: $command")
    isAuthorized(command) match
      case Authorized(isAuthorized) => if !isAuthorized then Fault(s"License is unauthorized: $command")
      case fault @ Fault(_, _) => fault
      case _ =>
        
    command match
      case Register(emailAddress)        => register(emailAddress)
      case Login(emailAddress, pin)      => login(emailAddress, pin)
      case Deactivate(license)           => deactivateAccount(license)
      case Reactivate(license)           => reactivateAccount(license)
      case ListProfiles(license)         => listProfiles()
      case AddProfile(_, profile)        => addProfile(profile)
      case UpdateProfile(_, profile)     => updateProfile(profile)
      case ListEdibles(_, profileId)     => listEdibles(profileId)
      case AddEdible(_, edible)          => addEdible(edible)
      case UpdateEdible(_, edible)       => updateEdible(edible)
      case ListDrinkables(_, profileId)  => listDrinkables(profileId)
      case AddDrinkable(_, drinkable)    => addDrinkable(drinkable)
      case UpdateDrinkable(_, drinkable) => updateDrinkable(drinkable)
      case _ => Fault("", 0) // TODO!

  private def isAuthorized(command: Command): Event =
    command match
      case license: License =>
        Try {
          Authorized( store.isAuthorized(license.license) )
        }.recover { case NonFatal(error) => Fault(s"Authorization failed: $error") }
         .get
      case Register(_) | Login(_, _) => Authorized(true)

  private def register(emailAddress: String): Event =
    Try {
      val account = Account(emailAddress = emailAddress)
      if store.isEmailAddressUnique(emailAddress) then
        email(account.emailAddress, account.pin)
        Registered( store.register(account) )
      else Fault(s"Registration failed because: $emailAddress is already registered.")
    }.recover { case NonFatal(error) => Fault(s"Registration failed for: $emailAddress, because: ${error.getMessage}") }
     .get

  private val subject = "Account Registration"

  private def email(emailAddress: String, pin: String): Unit =
    val recipients = List(emailAddress)
    val message = s"<p>Save this pin: <b>${pin}</b> in a safe place; then delete this email.</p>"
    emailer.send(recipients, subject, message)

  private def login(emailAddress: String, pin: String): Event =
    Try { store.login(emailAddress, pin) }.fold(
      error => Fault("Login failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then LoggedIn(optionalAccount.get)
        else Fault(s"Login failed for email address: $emailAddress and pin: $pin")
    )

  private def deactivateAccount(license: String): Event =
    Try { store.deactivateAccount(license) }.fold(
      error => Fault("Deactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Deactivated(optionalAccount.get)
        else Fault(s"Deactivate account failed for license: $license")
    )

  private def reactivateAccount(license: String): Event =
    Try { store.reactivateAccount(license) }.fold(
      error => Fault("Reactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Reactivated(optionalAccount.get)
        else Fault(s"Reactivate account failed for license: $license")
    )

  private def listProfiles(): Event =
    Try {
      ProfilesListed( store.listProfiles() )
    }.recover { case NonFatal(error) => Fault("List profiles failed:", error) }
     .get

  private def addProfile(profile: Profile): Event =
    Try {
      ProfileAdded( store.addProfile(profile) )
    }.recover { case NonFatal(error) => Fault("Add profile failed:", error) }
     .get

  private def updateProfile(profile: Profile): Event =
    Try {
      ProfileAdded( store.updateProfile(profile) )
    }.recover { case NonFatal(error) => Fault("Update profile failed:", error) }
     .get

  private def listEdibles(profileId: Long): Event =
    Try {
      EdiblesListed( store.listEdibles(profileId) )
    }.recover { case NonFatal(error) => Fault("List edibles failed:", error) }
     .get

  private def addEdible(edible: Edible): Event =
    Try {
      EdibleAdded( store.addEdible(edible) )
    }.recover { case NonFatal(error) => Fault("Add edible failed:", error) }
     .get

  private def updateEdible(edible: Edible): Event =
    Try {
      EdibleUpdated( store.updateEdible(edible) )
    }.recover { case NonFatal(error) => Fault("Update edible failed:", error) }
     .get

  private def listDrinkables(profileId: Long): Event =
    Try {
      DrinkablesListed( store.listDrinkables(profileId) )
    }.recover { case NonFatal(error) => Fault("List drinkables failed:", error) }
     .get

  private def addDrinkable(drinkable: Drinkable): Event =
    Try {
      DrinkableAdded( store.addDrinkable(drinkable) )
    }.recover { case NonFatal(error) => Fault("Add drinkable failed:", error) }
     .get

  private def updateDrinkable(drinkable: Drinkable): Event =
    Try {
      DrinkableUpdated( store.updateDrinkable(drinkable) )
    }.recover { case NonFatal(error) => Fault("Update drinkable failed:", error) }
     .get

  private def listExpendables(profileId: Long): Event =
    Try {
      ExpendablesListed( store.listExpendables(profileId) )
    }.recover { case NonFatal(error) => Fault("List expendables failed:", error) }
     .get

  private def addExpendable(expendable: Expendable): Event =
    Try {
      ExpendableAdded( store.addExpendable(expendable) )
    }.recover { case NonFatal(error) => Fault("Add expendable failed:", error) }
     .get

  private def updateExpendable(expendable: Expendable): Event =
    Try {
      ExpendableUpdated( store.updateExpendable(expendable) )
    }.recover { case NonFatal(error) => Fault("Update expendable failed:", error) }
     .get

  private def listMeasurables(profileId: Long): Event =
    Try {
      MeasurablesListed( store.listMeasurables(profileId) )
    }.recover { case NonFatal(error) => Fault("List measurables failed:", error) }
     .get