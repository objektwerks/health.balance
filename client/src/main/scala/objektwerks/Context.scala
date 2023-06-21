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