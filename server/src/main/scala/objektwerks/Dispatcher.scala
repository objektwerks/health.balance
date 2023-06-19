package objektwerks

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