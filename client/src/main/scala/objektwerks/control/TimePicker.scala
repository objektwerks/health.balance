package objektwerks.control

import java.time.LocalTime

import scalafx.geometry.Insets
import scalafx.scene.control.{Label, Spinner}
import scalafx.scene.layout.{HBox, Priority}

final class TimePicker(localTime: LocalTime) extends HBox:
  spacing = 3
  padding = Insets(3)

  val labelHour = Label("H:")
  val labelMinute = Label("M:")
  
  val hourSpinner = Spinner[Int](min = 0, max = 23, initialValue = localTime.getHour, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = localTime.getMinute, amountToStepBy = 1)

  children = List(labelHour, hourSpinner, labelMinute, minuteSpinner)
  HBox.setHgrow(this, Priority.Always)