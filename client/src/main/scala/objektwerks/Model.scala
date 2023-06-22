package objektwerks

import com.typesafe.scalalogging.LazyLogging

import scalafx.application.Platform
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer

final class Model(fetcher: Fetcher) extends LazyLogging:
  val shouldBeInFxThread = (message: String) => require(Platform.isFxApplicationThread, message)
  val shouldNotBeInFxThread = (message: String) => require(!Platform.isFxApplicationThread, message)

  val registered = ObjectProperty[Boolean](true)
  val loggedin = ObjectProperty[Boolean](true)

  val selectedProfileId = ObjectProperty[Long](0)
  val selectedEdibleId = ObjectProperty[Long](0)
  val selectedDrinkableId = ObjectProperty[Long](0)
  val selectedExpendableId = ObjectProperty[Long](0)
  val selectedMeasurableId = ObjectProperty[Long](0)

  selectedProfileId.onChange { (_, oldProfileId, newProfileId) =>
    shouldBeInFxThread("selected profile id onchange should be in fx thread.")
    logger.info(s"selected profile id onchange event: $oldProfileId -> $newProfileId")
    /*
    edibles(newProfileId)
    drinkables(newProfileId)
    expendables(newProfileId)
    measurables(newProfileId)
    */
  }

  val observableAccount = ObjectProperty[Account](Account.empty)
  val observableProfiles = ObservableBuffer[Profile]()
  val observableEdibles = ObservableBuffer[Edible]()
  val observableDrinkables = ObservableBuffer[Drinkable]()
  val observableExpendables = ObservableBuffer[Expendable]()
  val observableMeasurables = ObservableBuffer[Measurable]()
  val observableFaults = ObservableBuffer[Fault]()

  def onFault(cause: String): Unit =
    observableFaults += Fault(cause)
    logger.error(cause)

  def onFault(error: Throwable, cause: String): Unit =
    observableFaults += Fault(cause)
    logger.error(cause, error)

  def onFault(source: String, fault: Fault): Unit =
    observableFaults += fault
    logger.error(s"*** $source - $fault")

  def onFault(source: String, entity: Entity, fault: Fault): Unit =
    observableFaults += fault
    logger.error(s"*** $source - $entity - $fault")

  def register(register: Register): Unit =
    fetcher.fetch(
      register,
      (event: Event) => event match
        case fault @ Fault(_, _) => registered.set(false)
        case Registered(account) => observableAccount.set(account)
        case _ => ()
    )

  def login(login: Login): Unit =
    fetcher.fetch(
      login,
      (event: Event) => event match
        case fault @ Fault(_, _) => loggedin.set(false)
        case LoggedIn(account) => observableAccount.set(account)
        case _ => ()
    )

  def deactivate(deactivate: Deactivate): Unit =
    fetcher.fetchAsync(
      deactivate,
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.deactivate", fault)
        case Deactivated(account) => observableAccount.set(account)
        case _ => ()
    )

  def reactivate(reactivate: Reactivate): Unit =
    fetcher.fetchAsync(
      reactivate,
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.reactivate", fault)
        case Reactivated(account) => observableAccount.set(account)
        case _ => ()
    )

  def profiles(): Unit =
    fetcher.fetchAsync(
      ListProfiles(observableAccount.get.license),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.profiles", fault)
        case ProfilesListed(profiles) =>
          observableProfiles.clear()
          observableProfiles ++= profiles
        case _ => ()
    )

  def add(profile: Profile): Unit =
    fetcher.fetchAsync(
      AddProfile(observableAccount.get.license, profile),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.add profile", profile, fault)
        case ProfileAdded(id) =>
          observableProfiles += profile.copy(id = id)
          observableProfiles.sort()
          selectedProfileId.set(profile.id)
        case _ => ()
    )

  def update(profile: Profile): Unit =
    fetcher.fetchAsync(
      AddProfile(observableAccount.get.license, profile),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.update profile", profile, fault)
        case ProfileAdded(id) => observableProfiles.update(observableProfiles.indexOf(profile), profile)
        case _ => ()
    )

  def edibles(profileId: Long): Unit =
    fetcher.fetchAsync(
      ListEdibles(observableAccount.get.license, profileId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.edibles", fault)
        case EdiblesListed(edibles) =>
          observableEdibles.clear()
          observableEdibles ++= edibles
        case _ => ()
    )

  def add(edible: Edible): Unit =
    fetcher.fetchAsync(
      AddEdible(observableAccount.get.license, edible),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.add edible", edible, fault)
        case EdibleAdded(id) =>
          observableEdibles += edible.copy(id = id)
          observableEdibles.sort()
          selectedEdibleId.set(edible.id)
        case _ => ()
    )

  def update(edible: Edible): Unit =
    fetcher.fetchAsync(
      AddEdible(observableAccount.get.license, edible),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.update edible", edible, fault)
        case EdibleAdded(id) => observableEdibles.update(observableEdibles.indexOf(edible), edible)
        case _ => ()
    )

  def drinkables(profileId: Long): Unit =
    fetcher.fetchAsync(
      ListDrinkables(observableAccount.get.license, profileId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.drinkables", fault)
        case DrinkablesListed(drinkables) =>
          observableDrinkables.clear()
          observableDrinkables ++= drinkables
        case _ => ()
    )

  def add(drinkable: Drinkable): Unit =
    fetcher.fetchAsync(
      AddDrinkable(observableAccount.get.license, drinkable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFault("Model.add drinkable", drinkable, fault)
        case DrinkableAdded(id) =>
          observableDrinkables += drinkable.copy(id = id)
          observableDrinkables.sort()
          selectedDrinkableId.set(drinkable.id)
        case _ => ()
    )