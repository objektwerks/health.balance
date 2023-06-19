package objektwerks

object Validator:
  extension (value: String)
    def isLicense: Boolean = if value.nonEmpty && value.length == 36 then true else false
    def isPin: Boolean = value.length == 7
    def isEmailAddress: Boolean = value.nonEmpty && value.length >= 3 && value.contains("@")
    def isInt(text: String): Boolean = text.matches("\\d+")
    def isDouble(text: String): Boolean = text.matches("\\d{0,7}([\\.]\\d{0,4})?")

  extension (command: Command)
    def isValid: Boolean =
      command match
        case register @ Register(emailAddress)         => register.isValid
        case login @ Login(_, _)                       => login.isValid
        case deactivate @ Deactivate(_)                => deactivate.isValid
        case reactivate @ Reactivate(_)                => reactivate.isValid
        case _ => false // TODO!

  extension (register: Register)
    def isValid: Boolean = register.emailAddress.isEmailAddress

  extension (login: Login)
    def isValid: Boolean = login.emailAddress.isEmailAddress && login.pin.isPin

  extension (deactivate: Deactivate)
    def isValid: Boolean = deactivate.license.isLicense

  extension (reactivate: Reactivate)
    def isValid: Boolean = reactivate.license.isLicense

  extension  (license: License)
    def isLicense: Boolean = license.license.isLicense

  extension (account: Account)
    def isActivated: Boolean =
      account.id >= 0 &&
      account.license.isLicense &&
      account.emailAddress.isEmailAddress &&
      account.pin.isPin &&
      account.activated > 0 &&
      account.deactivated == 0
    def isDeactivated: Boolean =
      account.id > 0 &&
      account.license.isLicense &&
      account.emailAddress.isEmailAddress &&
      account.pin.isPin &&
      account.activated == 0 &&
      account.deactivated > 0

  extension (profile: Profile)
    def isValid =
      profile.id >= 0 &&
      profile.accountId > 0 &&
      profile.name.nonEmpty &&
      profile.created > 0

  extension (edible: Edible)
    def isValid =
      edible.id >= 0 &&
      edible.profileId > 0 &&
      edible.kind.nonEmpty &&
      edible.detail.nonEmpty &&
      edible.calories > 0 &&
      edible.ate > 0

  extension (drinkable: Drinkable)
    def isValid =
      drinkable.id >= 0 &&
      drinkable.profileId > 0 &&
      drinkable.kind.nonEmpty &&
      drinkable.detail.nonEmpty &&
      drinkable.count > 0 &&
      drinkable.calories >= 0 &&
      drinkable.drank > 0
