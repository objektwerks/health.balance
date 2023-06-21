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