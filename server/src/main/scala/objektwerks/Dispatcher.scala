package objektwerks

import scala.util.Try
import scala.util.control.NonFatal

final class Dispatcher(store: Store, emailer: Emailer):
  def dispatch[E <: Event](command: Command): Event =
    if !command.isValid then Fault(s"Command is invalid: $command")
    isAuthorized(command) match
      case Authorized(isAuthorized) => if !isAuthorized then Fault(s"License is unauthorized: $command")
      case fault @ Fault(_, _) => fault
      case _ =>
        
    command match
      case Register(emailAddress)          => register(emailAddress)
      case Login(emailAddress, pin)        => login(emailAddress, pin)
      case Deactivate(license)             => deactivateAccount(license)
      case Reactivate(license)             => reactivateAccount(license)
      case ListProfiles(license)           => listProfiles(license)
      case AddProfile(_, profile)          => addProfile(profile)

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