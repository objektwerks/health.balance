package objektwerks.control

import scalafx.geometry.Insets
import scalafx.scene.control.{Label, Spinner}
import scalafx.scene.layout.{HBox, Priority}

final class TimePicker(hour: Int, minute: Int) extends HBox:
  spacing = 3
  padding = Insets(3)

  val labelHour = Label("H:")
  val labelMinute = Label("M:")
  
  val hourSpinner = Spinner[Int](min = 0, max = 23, initialValue = hour, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = minute, amountToStepBy = 1)

  children = List(labelHour, hourSpinner, labelMinute, minuteSpinner)
  HBox.setHgrow(this, Priority.Always)