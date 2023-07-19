package objektwerks

import com.typesafe.config.ConfigFactory

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration.*
import scala.sys.process.Process

import Validator.*

class IntegrationTest extends AnyFunSuite with Matchers:
  val exitCode = Process("psql -d healthbalance -f ddl.sql").run().exitValue()
  exitCode shouldBe 0

  val config = ConfigFactory.load("test.conf")

  val store = Store(config, Store.cache(minSize = 1, maxSize = 1, expireAfter = 1.hour))
  val emailer = Emailer(config)
  val dispatcher = Dispatcher(store, emailer)

  var testAccount = Account()
  var testProfile = Profile(accountId = testAccount.id, name = "Fred")
  var testEdible = Edible(profileId = testProfile.id, detail = "apple", calories = 80)
  var testDrinkable = Drinkable(profileId = testProfile.id, detail = "juice", calories = 100)
  var testExpendable = Expendable(profileId = testProfile.id, detail = "walk", calories = 300)
  var testMeasurable = Measurable(profileId = testProfile.id, measurement = 60)

  test("integration") {
    register
    login

    deactivate
    reactivate

    addProfile
    updateProfile
    listProfiles

    addEdible
    updateEdible
    listEdibles

    addDrinkable
    updateDrinkable
    listDrinkables

    addExpendable
    updateExpendable
    listExpendables

    addMeasurable
    updateMeasurable
    listMeasurables

    fault
  }

  def register: Unit =
    val register = Register(config.getString("email.sender"))
    dispatcher.dispatch(register) match
      case Registered(account) =>
        assert( account.isActivated )
        testAccount = account
      case fault => fail(s"Invalid registered event: $fault")

  def login: Unit =
    val login = Login(testAccount.emailAddress, testAccount.pin)
    dispatcher.dispatch(login) match
      case LoggedIn(account) => account shouldBe testAccount
      case fault => fail(s"Invalid loggedin event: $fault")

  def deactivate: Unit =
    val deactivate = Deactivate(testAccount.license)
    dispatcher.dispatch(deactivate) match
      case Deactivated(account) => assert( account.isDeactivated )
      case fault => fail(s"Invalid deactivated event: $fault")

  def reactivate: Unit =
    val reactivate = Reactivate(testAccount.license)
    dispatcher.dispatch(reactivate) match
      case Reactivated(account) => assert( account.isActivated )
      case fault => fail(s"Invalid reactivated event: $fault")

  def addProfile: Unit =
    testProfile = testProfile.copy(accountId = testAccount.id)
    val addProfile = AddProfile(testAccount.license, testProfile)
    dispatcher.dispatch(addProfile) match
      case ProfileAdded(id) =>
        id should not be 0
        testProfile = testProfile.copy(id = id)
        testEdible = testEdible.copy(profileId = id)
        testDrinkable = testDrinkable.copy(profileId = id)
        testExpendable = testExpendable.copy(profileId = id)
        testMeasurable = testMeasurable.copy(profileId = id)
      case fault => fail(s"Invalid profile added event: $fault")

  def updateProfile: Unit =
    testProfile = testProfile.copy(name = "Fred Flintstone")
    val updateProfile = UpdateProfile(testAccount.license, testProfile)
    dispatcher.dispatch(updateProfile) match
      case ProfileUpdated(id) => id shouldBe testProfile.id
      case fault => fail(s"Invalid profile updated event: $fault")

  def listProfiles: Unit =
    val listProfiles = ListProfiles(testAccount.license)
    dispatcher.dispatch(listProfiles) match
      case ProfilesListed(profiles) =>
        profiles.length shouldBe 1
        profiles.head shouldBe testProfile
      case fault => fail(s"Invalid profiles listed event: $fault")

  def addEdible: Unit =
    val addEdible = AddEdible(testAccount.license, testEdible)
    dispatcher.dispatch(addEdible) match
      case EdibleAdded(id) =>
        id should not be 0
        testEdible = testEdible.copy(id = id)
      case fault => fail(s"Invalid edible added event: $fault")

  def updateEdible: Unit =
    testEdible = testEdible.copy(detail = "fuji apple")
    val updateEdible = UpdateEdible(testAccount.license, testEdible)
    dispatcher.dispatch(updateEdible) match
      case EdibleUpdated(id) => id shouldBe testEdible.id
      case fault => fail(s"Invalid edible updated event: $fault")

  def listEdibles: Unit =
    val listEdibles = ListEdibles(testAccount.license, testProfile.id)
    dispatcher.dispatch(listEdibles) match
      case EdiblesListed(edibles) =>
        edibles.length shouldBe 1
        edibles.head shouldBe testEdible
      case fault => fail(s"Invalid edibles listed event: $fault")

  def addDrinkable: Unit =
    val addDrinkable = AddDrinkable(testAccount.license, testDrinkable)
    dispatcher.dispatch(addDrinkable) match
      case DrinkableAdded(id) =>
        id should not be 0
        testDrinkable = testDrinkable.copy(id = id)
      case fault => fail(s"Invalid drikable added event: $fault")

  def updateDrinkable: Unit =
    testDrinkable = testDrinkable.copy(detail = "orange juice")
    val updateDrinkable = UpdateDrinkable(testAccount.license, testDrinkable)
    dispatcher.dispatch(updateDrinkable) match
      case DrinkableUpdated(id) => id shouldBe testDrinkable.id
      case fault => fail(s"Invalid drinkable updated event: $fault")

  def listDrinkables: Unit =
    val listDrinkables = ListDrinkables(testAccount.license, testProfile.id)
    dispatcher.dispatch(listDrinkables) match
      case DrinkablesListed(drinkables) =>
        drinkables.length shouldBe 1
        drinkables.head shouldBe testDrinkable
      case fault => fail(s"Invalid drinkables listed event: $fault")

  def addExpendable: Unit =
    val addExpendable = AddExpendable(testAccount.license, testExpendable)
    dispatcher.dispatch(addExpendable) match
      case ExpendableAdded(id) =>
        id should not be 0
        testExpendable = testExpendable.copy(id = id)
      case fault => fail(s"Invalid expendable added event: $fault")

  def updateExpendable: Unit =
    testExpendable = testExpendable.copy(detail = "5 mile walk")
    val updateExpendable = UpdateExpendable(testAccount.license, testExpendable)
    dispatcher.dispatch(updateExpendable) match
      case ExpendableUpdated(id) => id shouldBe testExpendable.id
      case fault => fail(s"Invalid expendable updated event: $fault")

  def listExpendables: Unit =
    val listExpendables = ListExpendables(testAccount.license, testProfile.id)
    dispatcher.dispatch(listExpendables) match
      case ExpendablesListed(expendables) =>
        expendables.length shouldBe 1
        expendables.head shouldBe testExpendable
      case fault => fail(s"Invalid expendables listed event: $fault")

  def addMeasurable: Unit =
    val addMeasurable = AddMeasurable(testAccount.license, testMeasurable)
    dispatcher.dispatch(addMeasurable) match
      case MeasurableAdded(id) =>
        id should not be 0
        testMeasurable = testMeasurable.copy(id = id)
      case fault => fail(s"Invalid measurable added event: $fault")

  def updateMeasurable: Unit =
    testMeasurable = testMeasurable.copy(measurement = 59)
    val updateMeasurable = UpdateMeasurable(testAccount.license, testMeasurable)
    dispatcher.dispatch(updateMeasurable) match
      case MeasurableUpdated(id) => id shouldBe testMeasurable.id
      case fault => fail(s"Invalid measurable updated event: $fault")

  def listMeasurables: Unit =
    val listMeasurables = ListMeasurables(testAccount.license, testProfile.id)
    dispatcher.dispatch(listMeasurables) match
      case MeasurablesListed(measurables) =>
        measurables.length shouldBe 1
        measurables.head shouldBe testMeasurable
      case fault => fail(s"Invalid measurables listed event: $fault")

  def fault: Unit =
    val fault = Fault("cause")
    store.addFault(fault) shouldBe fault
    store.listFaults().length shouldBe 1