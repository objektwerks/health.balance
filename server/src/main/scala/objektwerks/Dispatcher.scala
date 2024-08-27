package objektwerks

import ox.{IO, supervised}
import ox.resilience.{retry, RetryConfig}

import scala.concurrent.duration.*
import scala.util.Try
import scala.util.control.NonFatal

import Validator.*

final class Dispatcher(store: Store, emailer: Emailer):
  def dispatch(command: Command): Event =
    IO.unsafe:
      command.isValid match
        case false => addFault( Fault(s"Invalid command: $command") )
        case true =>
          isAuthorized(command) match
            case Unauthorized(cause) => addFault( Fault(cause) )
            case Authorized =>
              command match
                case Register(emailAddress)          => register(emailAddress)
                case Login(emailAddress, pin)        => login(emailAddress, pin)
                case Deactivate(license)             => deactivateAccount(license)
                case Reactivate(license)             => reactivateAccount(license)
                case ListProfiles(_, accountId)      => listProfiles(accountId)
                case AddProfile(_, profile)          => addProfile(profile)
                case UpdateProfile(_, profile)       => updateProfile(profile)
                case ListEdibles(_, profileId)       => listEdibles(profileId)
                case AddEdible(_, edible)            => addEdible(edible)
                case UpdateEdible(_, edible)         => updateEdible(edible)
                case ListDrinkables(_, profileId)    => listDrinkables(profileId)
                case AddDrinkable(_, drinkable)      => addDrinkable(drinkable)
                case UpdateDrinkable(_, drinkable)   => updateDrinkable(drinkable)
                case ListExpendables(_, profileId)   => listExpendables(profileId)
                case AddExpendable(_, expendable)    => addExpendable(expendable)
                case UpdateExpendable(_, expendable) => updateExpendable(expendable)
                case ListMeasurables(_, profileId)   => listMeasurables(profileId)
                case AddMeasurable(_, measurable)    => addMeasurable(measurable)
                case UpdateMeasurable(_, measurable) => updateMeasurable(measurable)
                case AddFault(_, fault)              => addFault(fault)

  private def isAuthorized(command: Command)(using IO): Security =
    command match
      case license: License =>
        Try:
          supervised:
            retry( RetryConfig.delay(1, 100.millis) )(
              if store.isAuthorized(license.license) then Authorized
              else Unauthorized(s"Unauthorized: $command")
            )
        .recover:
          case NonFatal(error) => Unauthorized(s"Unauthorized: $command, cause: $error")
        .get
      case Register(_) | Login(_, _) => Authorized

  private def sendEmail(emailAddress: String, message: String): Unit =
    val recipients = List(emailAddress)
    emailer.send(recipients, message)

  private def register(emailAddress: String)(using IO): Event =
    Try:
      supervised:
        val account = Account(emailAddress = emailAddress)
        val message = s"Your new pin is: ${account.pin}\n\nWelcome aboard!"
        retry( RetryConfig.delay(1, 600.millis) )( sendEmail(account.emailAddress, message) )
        Registered( store.register(account) )
    .recover:
      case NonFatal(error) => Fault(s"Registration failed for: $emailAddress, because: ${error.getMessage}")
    .get

  private def login(emailAddress: String, pin: String)(using IO): Event =
    Try:
      supervised:
        retry( RetryConfig.delay(1, 100.millis) )( store.login(emailAddress, pin) )
    .fold(
      error => Fault("Login failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then LoggedIn(optionalAccount.get)
        else Fault(s"Login failed for email address: $emailAddress and pin: $pin")
    )

  private def deactivateAccount(license: String)(using IO): Event =
    Try:
      supervised:
        retry( RetryConfig.delay(1, 100.millis) )( store.deactivateAccount(license) )
    .fold(
      error => Fault("Deactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Deactivated(optionalAccount.get)
        else Fault(s"Deactivate account failed for license: $license")
    )

  private def reactivateAccount(license: String)(using IO): Event =
    Try:
      supervised:
        retry( RetryConfig.delay(1, 100.millis) )( store.reactivateAccount(license) )
    .fold(
      error => Fault("Reactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Reactivated(optionalAccount.get)
        else Fault(s"Reactivate account failed for license: $license")
    )

  private def listProfiles(accountId: Long)(using IO): Event =
    Try:
      ProfilesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listProfiles(accountId) )
      )
    .recover:
      case NonFatal(error) => Fault("List profiles failed:", error)
    .get

  private def addProfile(profile: Profile)(using IO): Event =
    Try:
      ProfileAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addProfile(profile) )
      )
    .recover:
      case NonFatal(error) => Fault("Add profile failed:", error)
    .get

  private def updateProfile(profile: Profile)(using IO): Event =
    Try:
      ProfileUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateProfile(profile) )
      )
    .recover:
      case NonFatal(error) => Fault("Update profile failed:", error)
    .get

  private def listEdibles(profileId: Long)(using IO): Event =
    Try:
      EdiblesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listEdibles(profileId) )
      )
    .recover:
      case NonFatal(error) => Fault("List edibles failed:", error)
    .get

  private def addEdible(edible: Edible)(using IO): Event =
    Try:
      EdibleAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addEdible(edible) )
      )
    .recover:
      case NonFatal(error) => Fault("Add edible failed:", error)
    .get

  private def updateEdible(edible: Edible)(using IO): Event =
    Try:
      EdibleUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateEdible(edible) )
      )
    .recover:
      case NonFatal(error) => Fault("Update edible failed:", error)
    .get

  private def listDrinkables(profileId: Long)(using IO): Event =
    Try:
      DrinkablesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listDrinkables(profileId) )
      )
    .recover:
      case NonFatal(error) => Fault("List drinkables failed:", error)
    .get

  private def addDrinkable(drinkable: Drinkable)(using IO): Event =
    Try:
      DrinkableAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addDrinkable(drinkable) )
      )
    .recover:
      case NonFatal(error) => Fault("Add drinkable failed:", error)
    .get

  private def updateDrinkable(drinkable: Drinkable)(using IO): Event =
    Try:
      DrinkableUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateDrinkable(drinkable) )
      )
    .recover:
      case NonFatal(error) => Fault("Update drinkable failed:", error)
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

  private def addMeasurable(measurable: Measurable): Event =
    Try {
      MeasurableAdded( store.addMeasurable(measurable) )
    }.recover { case NonFatal(error) => Fault("Add measurable failed:", error) }
     .get

  private def updateMeasurable(measurable: Measurable): Event =
    Try {
      MeasurableUpdated( store.updateMeasurable(measurable) )
    }.recover { case NonFatal(error) => Fault("Updated measurable failed:", error) }
     .get

  private def addFault(fault: Fault): Event =
    Try {
      store.addFault(fault)
      FaultAdded()
    }.recover { case NonFatal(error) => Fault("Add fault failed:", error) }
     .get
