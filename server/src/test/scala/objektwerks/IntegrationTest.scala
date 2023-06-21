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
  var testProfile = Profile(accountId = testAccount.id, name = "Fred Flintstone")
  var testEdible = Edible(profileId = testProfile.id)
  var testDrinkable = Drinkable(profileId = testProfile.id)
  var testExpendable = Expendable(profileId = testProfile.id)
  var testMeasurable = Measurable(profileId = testProfile.id)

  test("integration") {
    register
    login

    deactivate
    reactivate
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
    val addProfile = AddProfile(testAccount.license, testProfile)
    dispatcher.dispatch(addProfile) match
      case ProfileAdded(id) =>
        id should not be 0
        testProfile = testProfile.copy(id = id)
        testEdible = testEdible.copy(profileId = id)
        testDrinkable = testDrinkable.copy(profileId = id)
        testExpendable = testExpendable.copy(profileId = id)
        testMeasurable = testMeasurable.copy(profileId = id)
      case fault => fail(s"Invalid pool saved event: $fault")