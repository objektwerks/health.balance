package objektwerks

import ox.supervised
import ox.resilience.retry
import ox.scheduling.Schedule

import scala.concurrent.duration.*
import scala.util.Try
import scala.util.control.NonFatal

import Validator.*

final class Dispatcher(store: Store, emailer: Emailer):
  def dispatch(command: Command): Event =
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

  private def isAuthorized(command: Command): Security =
    command match
      case license: License =>
        try
          supervised:
            retry( Schedule.fixedInterval(100.millis).maxAttempts(1) )(
              if store.isAuthorized(license.license) then Authorized
              else Unauthorized(s"Unauthorized: $command")
            )
        catch
          case NonFatal(error) => Unauthorized(s"Unauthorized: $command, cause: $error")
      case Register(_) | Login(_, _) => Authorized

  private def sendEmail(emailAddress: String, message: String): Boolean =
    val recipients = List(emailAddress)
    emailer.send(recipients, message)

  private def register(emailAddress: String): Event =
    try
      supervised:
        val account = Account(emailAddress = emailAddress)
        val message = s"Your new pin is: ${account.pin}\n\nWelcome aboard!"
        val result = retry( Schedule.fixedInterval(600.millis).maxAttempts(1) )( sendEmail(account.emailAddress, message) )
        if result then
          Registered( store.register(account) )
        else
          throw IllegalArgumentException("Invalid email address.")
    catch
      case NonFatal(error) => Fault(s"Registration failed for: $emailAddress, because: ${error.getMessage}")

  private def login(emailAddress: String, pin: String): Event =
    Try:
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxAttempts(1) )( store.login(emailAddress, pin) )
    .fold(
      error => Fault("Login failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then LoggedIn(optionalAccount.get)
        else Fault(s"Login failed for email address: $emailAddress and pin: $pin")
    )

  private def deactivateAccount(license: String): Event =
    Try:
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxAttempts(1) )( store.deactivateAccount(license) )
    .fold(
      error => Fault("Deactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Deactivated(optionalAccount.get)
        else Fault(s"Deactivate account failed for license: $license")
    )

  private def reactivateAccount(license: String): Event =
    Try:
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.reactivateAccount(license) )
    .fold(
      error => Fault("Reactivate account failed:", error),
      optionalAccount =>
        if optionalAccount.isDefined then Reactivated(optionalAccount.get)
        else Fault(s"Reactivate account failed for license: $license")
    )

  private def listProfiles(accountId: Long): Event =
    try
      ProfilesListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listProfiles(accountId) )
      )
    catch
      case NonFatal(error) => Fault("List profiles failed:", error)

  private def addProfile(profile: Profile): Event =
    try
      ProfileAdded(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addProfile(profile) )
      )
    catch
      case NonFatal(error) => Fault("Add profile failed:", error)

  private def updateProfile(profile: Profile): Event =
    try
      ProfileUpdated(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateProfile(profile) )
      )
    catch
      case NonFatal(error) => Fault("Update profile failed:", error)

  private def listEdibles(profileId: Long): Event =
    try
      EdiblesListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listEdibles(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List edibles failed:", error)

  private def addEdible(edible: Edible): Event =
    try
      EdibleAdded(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addEdible(edible) )
      )
    catch
      case NonFatal(error) => Fault("Add edible failed:", error)

  private def updateEdible(edible: Edible): Event =
    try
      EdibleUpdated(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateEdible(edible) )
      )
    catch
      case NonFatal(error) => Fault("Update edible failed:", error)

  private def listDrinkables(profileId: Long): Event =
    try
      DrinkablesListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listDrinkables(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List drinkables failed:", error)

  private def addDrinkable(drinkable: Drinkable): Event =
    try
      DrinkableAdded(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addDrinkable(drinkable) )
      )
    catch
      case NonFatal(error) => Fault("Add drinkable failed:", error)

  private def updateDrinkable(drinkable: Drinkable): Event =
    try
      DrinkableUpdated(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateDrinkable(drinkable) )
      )
    catch
      case NonFatal(error) => Fault("Update drinkable failed:", error)

  private def listExpendables(profileId: Long): Event =
    try
      ExpendablesListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listExpendables(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List expendables failed:", error)

  private def addExpendable(expendable: Expendable): Event =
    try
      ExpendableAdded(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addExpendable(expendable) )
      )
    catch
      case NonFatal(error) => Fault("Add expendable failed:", error)

  private def updateExpendable(expendable: Expendable): Event =
    try
      ExpendableUpdated(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateExpendable(expendable) )
      )
    catch
      case NonFatal(error) => Fault("Update expendable failed:", error)

  private def listMeasurables(profileId: Long): Event =
    try
      MeasurablesListed(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.listMeasurables(profileId) )
      )
    catch
      case NonFatal(error) => Fault("List measurables failed:", error)

  private def addMeasurable(measurable: Measurable): Event =
    try
      MeasurableAdded(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addMeasurable(measurable) )
      )
    catch
      case NonFatal(error) => Fault("Add measurable failed:", error)

  private def updateMeasurable(measurable: Measurable): Event =
    try
      MeasurableUpdated(
        supervised:
          retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.updateMeasurable(measurable) )
      )
    catch
      case NonFatal(error) => Fault("Updated measurable failed:", error)

  private def addFault(fault: Fault): Event =
    try
      supervised:
        retry( Schedule.fixedInterval(100.millis).maxRepeats(1) )( store.addFault(fault) )
        FaultAdded()
    catch
      case NonFatal(error) => Fault("Add fault failed:", error)