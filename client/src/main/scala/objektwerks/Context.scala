package objektwerks

import com.typesafe.config.Config

import scalafx.scene.image.{Image, ImageView}

final class Context(config: Config):
  val windowTitle = config.getString("window.title")
  val windowWidth = config.getDouble("window.width")
  val windowHeight = config.getDouble("window.height")

  val url = config.getString("url")

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

  val chartMin = config.getString("chart.min")
  val chartMax = config.getString("chart.max")
  val chartAvg = config.getString("chart.avg")
  val chartMonthDay = config.getString("chart.monthDay")
  val chartWeight = config.getString("chart.weight")
  val chartPulse = config.getString("chart.pulse")
  val chartGlucose = config.getString("chart.glucose")

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

  val errorServer = config.getString("error.server")
  val errorRegister = config.getString("error.register")
  val errorLogin = config.getString("error.login")

  val headerOccurred = config.getString("header.occurred")
  val headerFault = config.getString("header.fault")
  val headerName = config.getString("header.name")
  val headerCreated = config.getString("header.created")
  val headerKind = config.getString("header.kind")
  val headerDetail = config.getString("header.detail")
  val headerCalories = config.getString("header.calories")
  val headerAte = config.getString("header.ate")
  val headerOrganic = config.getString("header.organic")
  val headerCount = config.getString("header.count")
  val headerDrank = config.getString("header.drank")
  val headerSunshine = config.getString("header.sunshine")
  val headerFreshair = config.getString("header.freshair")
  val headerStart = config.getString("header.start")
  val headerFinish = config.getString("header.finish")
  val headerMeasurement = config.getString("header.measurement")
  val headerMeasured = config.getString("header.measured")

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
  val labelDateAte = config.getString("label.dateAte")
  val labelTimeAte = config.getString("label.timeAte")
  val labelDateDrank = config.getString("label.dateDrank")
  val labelTimeDrank = config.getString("label.timeDrank")
  val labelSunshine = config.getString("label.sunshine")
  val labelFreshair = config.getString("label.freshair")
  val labelDateStart= config.getString("label.dateStart")
  val labelTimeStart = config.getString("label.timeStart")
  val labelDateFinish = config.getString("label.dateFinish")
  val labelTimeFinish = config.getString("label.timeFinish")
  val labelMeasurement = config.getString("label.measurement")
  val labelUnit = config.getString("label.unit")
  val labelDateMeasured = config.getString("label.dateMeasured")
  val labelTimeMeasured = config.getString("label.timeMeasured")
  val labelToday = config.getString("label.today")
  val labelWeek = config.getString("label.week")
  val labelWeight = config.getString("label.weight")
  val labelPulse = config.getString("label.pulse")
  val labelGlucose = config.getString("label.glucose")

  val tabProfiles = config.getString("tab.profiles")
  val tabEdibles = config.getString("tab.edibles")
  val tabDrinkables = config.getString("tab.drinkables")
  val tabExpendables = config.getString("tab.expendables")
  val tabMeasurables = config.getString("tab.measurables")
  val tabCaloriesInOut = config.getString("tab.caloriesinout")
  val tabToday = config.getString("tab.today")
  val tabWeek = config.getString("tab.week")
  val tabWeight = config.getString("tab.weight")
  val tabPulse = config.getString("tab.pulse")
  val tabGlucose = config.getString("tab.glucose")

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