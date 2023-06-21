package objektwerks.dialog

import java.awt.Toolkit

import scalafx.scene.control.{Alert, ButtonType}
import scalafx.scene.control.Alert.AlertType
import scalafx.stage.Stage

import objektwerks.Context

object Alerts:
  val centerX = Toolkit.getDefaultToolkit.getScreenSize.width / 2.4
  val centerY = Toolkit.getDefaultToolkit.getScreenSize.height / 6

  def showRegisterAlert(context: Context, stage: Stage): Option[ButtonType] =
    new Alert(AlertType.Error) {
      initOwner(stage)
      title = context.windowTitle
      headerText = context.buttonRegister
      contentText = context.errorRegister
      x = centerX
      y = centerY
    }.showAndWait()

  def showLoginAlert(context: Context, stage: Stage): Option[ButtonType] =
    new Alert(AlertType.Error) {
      initOwner(stage)
      title = context.windowTitle
      headerText = context.buttonLogin
      contentText = context.errorLogin
      x = centerX
      y = centerY
    }.showAndWait()
