package objektwerks.control

import scalafx.geometry.Insets
import scalafx.scene.control.Spinner
import scalafx.scene.layout.{HBox, Priority}

final class TimePicker extends HBox:
  spacing = 3
  padding = Insets(3)
  
  val hourSpinner = Spinner[Int](min = 0, max = 23, initialValue = 1, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = 1, amountToStepBy = 1)

  children = List(hourSpinner, minuteSpinner)
  HBox.setHgrow(this, Priority.Always)