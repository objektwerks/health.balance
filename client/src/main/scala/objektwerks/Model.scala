package objektwerks

import com.typesafe.scalalogging.LazyLogging

import java.time.LocalDate

import scalafx.application.Platform
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.{Add, Update}

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
    logger.info(s"*** selected profile id onchange event: $oldProfileId -> $newProfileId")
    shouldBeInFxThread("*** selected profile id onchange should be in fx thread.")
    edibles(newProfileId)
    drinkables(newProfileId)
    expendables(newProfileId)
    measurables(newProfileId)
    Platform.runLater( dashboard() )
  }

  val objectAccount = ObjectProperty[Account](Account.empty)
  val observableProfiles = ObservableBuffer[Profile]()
  val observableEdibles = ObservableBuffer[Edible]()
  val observableDrinkables = ObservableBuffer[Drinkable]()
  val observableExpendables = ObservableBuffer[Expendable]()
  val observableMeasurables = ObservableBuffer[Measurable]()
  val observableFaults = ObservableBuffer[Fault]()

  objectAccount.onChange { (_, oldAccount, newAccount) =>
    logger.info(s"*** object account onchange event: $oldAccount -> $newAccount")
  }

  observableProfiles.onChange { (_, changes) =>
    logger.info(s"*** observable profiles onchange event: $changes")
  }

  val ediblesTodayCalories = ObjectProperty[String]("0")
  val ediblesWeekCalories = ObjectProperty[String]("0")

  def setEdiblesTodayCalories() =
    val today = LocalDate.now.getDayOfYear
    ediblesTodayCalories.value = observableEdibles.filter(e => Entity.epochSecondToDayOfYear(e.ate) == today).map(_.calories).sum.toString

  def setEdiblesWeekCalories() =
    val week = LocalDate.now.minusDays(8).toEpochDay
    ediblesWeekCalories.value = observableEdibles.filter(e => Entity.epochSecondToEpochDay(e.ate) > week).map(_.calories).sum.toString

  val drinkablesTodayCalories = ObjectProperty[String]("0")
  val drinkablesWeekCalories = ObjectProperty[String]("0")

  def setDrinkablesTodayCalories() =
    val today = LocalDate.now.getDayOfYear
    drinkablesTodayCalories.value = observableDrinkables.filter(d => Entity.epochSecondToDayOfYear(d.drank) == today).map(_.calories).sum.toString

  def setDrinkablesWeekCalories() =
    val week = LocalDate.now.minusDays(8).toEpochDay
    drinkablesWeekCalories.value = observableDrinkables.filter(d => Entity.epochSecondToEpochDay(d.drank) > week).map(_.calories).sum.toString

  val expendablesTodayCalories = ObjectProperty[String]("0")
  val expendablesWeekCalories = ObjectProperty[String]("0")

  def setExpendablesTodayCalories() =
    val today = LocalDate.now.getDayOfYear
    expendablesTodayCalories.value = observableExpendables.filter(e => Entity.epochSecondToDayOfYear(e.start) == today).map(_.calories).sum.toString

  def setExpendablesWeekCalories() =
    val week = LocalDate.now.minusDays(8).toEpochDay
    expendablesWeekCalories.value = observableExpendables.filter(e => Entity.epochSecondToEpochDay(e.start) > week).map(_.calories).sum.toString

  val caloriesInOutToday = ObjectProperty[String]("0/0")
  val caloriesInOutWeek = ObjectProperty[String]("0/0")

  def setCaloriesInOutToday() =
    val caloriesIn = ( ediblesTodayCalories.value.toInt + drinkablesTodayCalories.value.toInt ).toString
    val caloriesOut = expendablesTodayCalories.value
    caloriesInOutToday.value = s"$caloriesIn/$caloriesOut"

  def setCaloriesInOutWeek() =
    val caloriesIn = ( ediblesWeekCalories.value.toInt + drinkablesWeekCalories.value.toInt ).toString
    val caloriesOut = expendablesWeekCalories.value
    caloriesInOutToday.value = s"$caloriesIn/$caloriesOut"

  def setCaloriesInOut() =
    setCaloriesInOutToday()
    setCaloriesInOutWeek()

  observableEdibles.onChange { (_, changes) =>
    logger.info(s"*** observable edibles onchange event: $changes")
    shouldNotBeInFxThread("*** observable edibles onchange should not be in fx thread.")

    Platform.runLater {
      setEdiblesTodayCalories()
      setEdiblesWeekCalories()

      setCaloriesInOut()
    }
  }

  observableDrinkables.onChange { (_, changes) =>
    logger.info(s"*** observable drinkables onchange event: $changes")
    shouldNotBeInFxThread("*** observable drinkables onchange should not be in fx thread.")

    Platform.runLater {
      setDrinkablesTodayCalories()
      setDrinkablesWeekCalories()

      setCaloriesInOut()
    }
  }

  observableExpendables.onChange { (_, changes) =>
    logger.info(s"*** observable expendables onchange event: $changes")
    shouldNotBeInFxThread("*** observable expendables onchange should not be in fx thread.")

    Platform.runLater {
      setExpendablesTodayCalories()
      setExpendablesWeekCalories()

      setCaloriesInOut()
    }
  }

  val weightToday = ObjectProperty[String]("0")
  val weightWeek = ObjectProperty[String]("0")

  val pulseToday = ObjectProperty[String]("0")
  val pulseWeek = ObjectProperty[String]("0")

  val glucoseToday = ObjectProperty[String]("0")
  val glucoseWeek = ObjectProperty[String]("0")

  def setMeasurableToday(property: ObjectProperty[String], kind: String) =
    val today = LocalDate.now.getDayOfYear
    property.value = observableMeasurables
      .filter(m => m.kind == kind.toString)
      .filter(m => Entity.epochSecondToDayOfYear(m.measured) == today)
      .map(_.measurement)
      .sum
      .toString

  def setMeasurableWeek(property: ObjectProperty[String], kind: String) =
    val week = LocalDate.now.minusDays(8).toEpochDay
    property.value = observableMeasurables
      .filter(m => m.kind == kind.toString)
      .filter(m => Entity.epochSecondToEpochDay(m.measured) > week)
      .map(_.measurement)
      .sum
      .toString

  observableMeasurables.onChange { (_, changes) =>
    logger.info(s"*** observable measurables onchange event: $changes")
    shouldNotBeInFxThread("*** observable measurables onchange should not be in fx thread.")

    for (change <- changes)
      val kind = change match
        case Add(_, added)    => added.head.kind
        case Update(from, to) => observableMeasurables(from).kind
        case _ => ""

      val (today, week) = MeasurableKind.valueOf(kind) match
        case MeasurableKind.Weight => (weightToday, weightWeek)
        case MeasurableKind.Pulse => (pulseToday, pulseWeek)
        case MeasurableKind.Glucose => (glucoseToday, glucoseWeek)

      Platform.runLater {
        setMeasurableToday(today, kind)
        setMeasurableWeek(week, kind)
      }
  }

  def dashboard() =
    logger.info("*** dashboard reset...")
    shouldBeInFxThread("*** dashboard should be in fx thread.")

    setEdiblesTodayCalories()
    setEdiblesWeekCalories()

    setDrinkablesTodayCalories()
    setDrinkablesWeekCalories()

    setExpendablesTodayCalories()
    setExpendablesWeekCalories()

    setCaloriesInOut()

  def onFetchFault(source: String, fault: Fault): Unit =
    val cause = s"$source - $fault"
    logger.error(s"*** Cause: $cause")
    observableFaults += fault.copy(cause = cause)

  def onFetchFault(source: String, entity: Entity, fault: Fault): Unit =
    val cause = s"$source - $entity - $fault"
    logger.error(s"*** Cause: $cause")
    observableFaults += fault.copy(cause = cause)

  def add(fault: Fault): Unit =
    fetcher.fetchAsync(
      AddFault(objectAccount.get.license, fault),
      (event: Event) => event match
        case fault @ Fault(cause, _) => onFetchFault("Model.add fault", fault)
        case FaultAdded() =>
          observableFaults += fault
          observableFaults.sort()
        case _ => ()
    )

  def register(register: Register): Unit =
    fetcher.fetch(
      register,
      (event: Event) => event match
        case fault @ Fault(_, _) => registered.set(false)
        case Registered(account) => objectAccount.set(account)
        case _ => ()
    )

  def login(login: Login): Unit =
    fetcher.fetch(
      login,
      (event: Event) => event match
        case fault @ Fault(_, _) => loggedin.set(false)
        case LoggedIn(account) =>
          objectAccount.set(account)
          profiles()
        case _ => ()
    )

  def deactivate(deactivate: Deactivate): Unit =
    fetcher.fetchAsync(
      deactivate,
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.deactivate", fault)
        case Deactivated(account) => objectAccount.set(account)
        case _ => ()
    )

  def reactivate(reactivate: Reactivate): Unit =
    fetcher.fetchAsync(
      reactivate,
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.reactivate", fault)
        case Reactivated(account) => objectAccount.set(account)
        case _ => ()
    )

  def profiles(): Unit =
    fetcher.fetchAsync(
      ListProfiles(objectAccount.get.license),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.profiles", fault)
        case ProfilesListed(profiles) =>
          observableProfiles.clear()
          observableProfiles ++= profiles
        case _ => ()
    )

  def add(profile: Profile): Unit =
    fetcher.fetchAsync(
      AddProfile(objectAccount.get.license, profile),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.add profile", profile, fault)
        case ProfileAdded(id) =>
          observableProfiles += profile.copy(id = id)
          observableProfiles.sort()
          selectedProfileId.set(profile.id)
        case _ => ()
    )

  def update(profile: Profile): Unit =
    fetcher.fetchAsync(
      AddProfile(objectAccount.get.license, profile),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.update profile", profile, fault)
        case ProfileAdded(id) => observableProfiles.update(observableProfiles.indexOf(profile), profile)
        case _ => ()
    )

  def edibles(profileId: Long): Unit =
    fetcher.fetchAsync(
      ListEdibles(objectAccount.get.license, profileId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.edibles", fault)
        case EdiblesListed(edibles) =>
          observableEdibles.clear()
          observableEdibles ++= edibles
        case _ => ()
    )

  def add(edible: Edible): Unit =
    fetcher.fetchAsync(
      AddEdible(objectAccount.get.license, edible),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.add edible", edible, fault)
        case EdibleAdded(id) =>
          observableEdibles += edible.copy(id = id)
          observableEdibles.sort()
          selectedEdibleId.set(edible.id)
        case _ => ()
    )

  def update(edible: Edible): Unit =
    fetcher.fetchAsync(
      AddEdible(objectAccount.get.license, edible),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.update edible", edible, fault)
        case EdibleAdded(id) => observableEdibles.update(observableEdibles.indexOf(edible), edible)
        case _ => ()
    )

  def drinkables(profileId: Long): Unit =
    fetcher.fetchAsync(
      ListDrinkables(objectAccount.get.license, profileId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.drinkables", fault)
        case DrinkablesListed(drinkables) =>
          observableDrinkables.clear()
          observableDrinkables ++= drinkables
        case _ => ()
    )

  def add(drinkable: Drinkable): Unit =
    fetcher.fetchAsync(
      AddDrinkable(objectAccount.get.license, drinkable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.add drinkable", drinkable, fault)
        case DrinkableAdded(id) =>
          observableDrinkables += drinkable.copy(id = id)
          observableDrinkables.sort()
          selectedDrinkableId.set(drinkable.id)
        case _ => ()
    )

  def update(drinkable: Drinkable): Unit =
    fetcher.fetchAsync(
      AddDrinkable(objectAccount.get.license, drinkable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.update drinkable", drinkable, fault)
        case DrinkableAdded(id) => observableDrinkables.update(observableDrinkables.indexOf(drinkable), drinkable)
        case _ => ()
    )

  def expendables(profileId: Long): Unit =
    fetcher.fetchAsync(
      ListExpendables(objectAccount.get.license, profileId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.expendables", fault)
        case ExpendablesListed(expendables) =>
          observableExpendables.clear()
          observableExpendables ++= expendables
        case _ => ()
    )

  def add(expendable: Expendable): Unit =
    fetcher.fetchAsync(
      AddExpendable(objectAccount.get.license, expendable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.add expendable", expendable, fault)
        case ExpendableAdded(id) =>
          observableExpendables += expendable.copy(id = id)
          observableExpendables.sort()
          selectedExpendableId.set(expendable.id)
        case _ => ()
    )

  def update(expendable: Expendable): Unit =
    fetcher.fetchAsync(
      AddExpendable(objectAccount.get.license, expendable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.update expendable", expendable, fault)
        case ExpendableAdded(id) => observableExpendables.update(observableExpendables.indexOf(expendable), expendable)
        case _ => ()
    )

  def measurables(profileId: Long): Unit =
    fetcher.fetchAsync(
      ListMeasurables(objectAccount.get.license, profileId),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.measurables", fault)
        case MeasurablesListed(measurables) =>
          observableMeasurables.clear()
          observableMeasurables ++= measurables
        case _ => ()
    )

  def add(measurable: Measurable): Unit =
    fetcher.fetchAsync(
      AddMeasurable(objectAccount.get.license, measurable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.add measurable", measurable, fault)
        case MeasurableAdded(id) =>
          observableMeasurables += measurable.copy(id = id)
          observableMeasurables.sort()
          selectedMeasurableId.set(measurable.id)
        case _ => ()
    )

  def update(measurable: Measurable): Unit =
    fetcher.fetchAsync(
      AddMeasurable(objectAccount.get.license, measurable),
      (event: Event) => event match
        case fault @ Fault(_, _) => onFetchFault("Model.update measurable", measurable, fault)
        case MeasurableAdded(id) => observableMeasurables.update(observableMeasurables.indexOf(measurable), measurable)
        case _ => ()
    )