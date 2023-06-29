package objektwerks

import com.typesafe.config.Config

import scala.jdk.CollectionConverters.*
import scalafx.scene.image.{Image, ImageView}

final class Context(config: Config):
  val windowTitle = config.getString("window.title")
  val windowWidth = config.getDouble("window.width")
  val windowHeight = config.getDouble("window.height")

  val url = config.getString("url")

  val errorServer = config.getString("error.server")
  val errorRegister = config.getString("error.register")
  val errorLogin = config.getString("error.login")

  val buttonAdd = config.getString("button.add")
  val buttonEdit = config.getString("button.edit")
  val buttonSave = config.getString("button.save")
  val buttonChart = config.getString("button.chart")
  val buttonFaults = config.getString("button.faults")
  val buttonRegister = config.getString("button.register")
  val buttonLogin = config.getString("button.login")
  val buttonAccount = config.getString("button.account")
  val buttonActivate = config.getString("button.activate")
  val buttonDeactivate = config.getString("button.deactivate")

  val columnYes = config.getString("column.yes")
  val columnNo = config.getString("column.no")

  val dialogRegisterLogin = config.getString("dialog.registerLogin")
  val dialogAccount = config.getString("dialog.account")
  val dialogFaults = config.getString("dialog.faults")
  val dialogProfile = config.getString("dialog.profile")
  val dialogEdible = config.getString("dialog.edible")
  val dialogDrinkable = config.getString("dialog.drinkable")
  val dialogExpendable = config.getString("dialog.expendable")
  val dialogMeasurable = config.getString("dialog.measurable")

  val headerOccurred = config.getString("header.occurred")
  val headerFault = config.getString("header.fault")

  val labelLicense = config.getString("label.license")
  val labelEmailAddress = config.getString("label.emailAddress")
  val labelPin = config.getString("label.pin")
  val labelActivated = config.getString("label.activated")
  val labelDeactivated = config.getString("label.deactivated")
  val labelName = config.getString("label.name")
  val labelKind = config.getString("label.kind")
  val labelDetail = config.getString("label.detail")
  val labelOrganic = config.getString("label.organic")
  val labelCount = config.getString("label.count")
  val labelCalories = config.getString("label.calories")
  val labelAte = config.getString("label.ate")
  val labelDrank = config.getString("label.drank")
  val labelSunshine = config.getString("label.sunshine")
  val labelFreshair = config.getString("label.freshair")
  val labelStart = config.getString("label.start")
  val labelFinish = config.getString("label.finish")
  val labelMeasurement = config.getString("label.measurement")
  val labelUnit = config.getString("label.unit")
  val labelMeasured = config.getString("label.measured")

  val tabProfiles = config.getString("tab.profiles")

  def logoImage = loadImageView("/image/logo.png")
  def addImage = loadImageView("/image/add.png")
  def editImage = loadImageView("/image/edit.png")
  def chartImage = loadImageView("/image/chart.png")
  def faultsImage = loadImageView("/image/faults.png")
  def accountImage = loadImageView("/image/account.png")

  def logo = new Image(Image.getClass.getResourceAsStream("/image/logo.png"))

  private def loadImageView(path: String): ImageView = new ImageView:
    image = new Image(Image.getClass.getResourceAsStream(path))
    fitHeight = 25
    fitWidth = 25
    preserveRatio = true
    smooth = true