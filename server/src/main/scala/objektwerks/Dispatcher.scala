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
        try
          supervised:
            retry( RetryConfig.delay(1, 100.millis) )(
              if store.isAuthorized(license.license) then Authorized
              else Unauthorized(s"Unauthorized: $command")
            )
        catch
          case NonFatal(error) => Unauthorized(s"Unauthorized: $command, cause: $error")
      case Register(_) | Login(_, _) => Authorized

  private def sendEmail(emailAddress: String, message: String): Unit =
    val recipients = List(emailAddress)
    emailer.send(recipients, message)

  private def register(emailAddress: String)(using IO): Event =
    try
      supervised:
        val account = Account(emailAddress = emailAddress)
        val message = s"Your new pin is: ${account.pin}\n\nWelcome aboard!"
        retry( RetryConfig.delay(1, 600.millis) )( sendEmail(account.emailAddress, message) )
        Registered( store.register(account) )
    catch
      case NonFatal(error) => Fault(s"Registration failed for: $emailAddress, because: ${error.getMessage}")

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
    try
      ProfilesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listProfiles(accountId) )
      )
    catch
      case NonFatal(error) => Fault("List profiles failed:", error)

  private def addProfile(profile: Profile)(using IO): Event =
    try
      ProfileAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addProfile(profile) )
      )
    catch
      case NonFatal(error) => Fault("Add profile failed:", error)

  private def updateProfile(profile: Profile)(using IO): Event =
    try
      ProfileUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateProfile(profile) )
      )
    catch
      case NonFatal(error) => Fault("Update profile failed:", error)

  private def listEdibles(profileId: Long)(using IO): Event =
    try
      EdiblesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listEdibles(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List edibles failed:", error)

  private def addEdible(edible: Edible)(using IO): Event =
    try
      EdibleAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addEdible(edible) )
      )
    catch
      case NonFatal(error) => Fault("Add edible failed:", error)

  private def updateEdible(edible: Edible)(using IO): Event =
    try
      EdibleUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateEdible(edible) )
      )
    catch
      case NonFatal(error) => Fault("Update edible failed:", error)

  private def listDrinkables(profileId: Long)(using IO): Event =
    try
      DrinkablesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listDrinkables(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List drinkables failed:", error)

  private def addDrinkable(drinkable: Drinkable)(using IO): Event =
    try
      DrinkableAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addDrinkable(drinkable) )
      )
    catch
      case NonFatal(error) => Fault("Add drinkable failed:", error)

  private def updateDrinkable(drinkable: Drinkable)(using IO): Event =
    try
      DrinkableUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateDrinkable(drinkable) )
      )
    catch
      case NonFatal(error) => Fault("Update drinkable failed:", error)

  private def listExpendables(profileId: Long)(using IO): Event =
    try
      ExpendablesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listExpendables(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List expendables failed:", error)

  private def addExpendable(expendable: Expendable)(using IO): Event =
    try
      ExpendableAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addExpendable(expendable) )
      )
    catch
      case NonFatal(error) => Fault("Add expendable failed:", error)

  private def updateExpendable(expendable: Expendable)(using IO): Event =
    Try:
      ExpendableUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateExpendable(expendable) )
      )
    .recover:
      case NonFatal(error) => Fault("Update expendable failed:", error)
    .get

  private def listMeasurables(profileId: Long)(using IO): Event =
    Try:
      MeasurablesListed(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.listMeasurables(profileId) )
      )
    .recover:
      case NonFatal(error) => Fault("List measurables failed:", error)
    .get

  private def addMeasurable(measurable: Measurable)(using IO): Event =
    Try:
      MeasurableAdded(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.addMeasurable(measurable) )
      )
    .recover:
      case NonFatal(error) => Fault("Add measurable failed:", error)
    .get

  private def updateMeasurable(measurable: Measurable)(using IO): Event =
    Try:
      MeasurableUpdated(
        supervised:
          retry( RetryConfig.delay(1, 100.millis) )( store.updateMeasurable(measurable) )
      )
    .recover:
      case NonFatal(error) => Fault("Updated measurable failed:", error)
    .get

  private def addFault(fault: Fault)(using IO): Event =
    Try:
      supervised:
        retry( RetryConfig.delay(1, 100.millis) )( store.addFault(fault) )
        FaultAdded()
    .recover:
      case NonFatal(error) => Fault("Add fault failed:", error)
    .get
