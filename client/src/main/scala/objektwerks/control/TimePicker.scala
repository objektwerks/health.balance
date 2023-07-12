package objektwerks.control

import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.Spinner
import scalafx.scene.layout.{HBox, Priority}

final class TimePicker extends HBox:
  spacing = 6
  padding = Insets(6)

  val ampm = ObservableBuffer[String]("AM", "PM")
  
  val hourSpinner = Spinner[Int](min = 0, max = 12, initialValue = 1, amountToStepBy = 1)
  val minuteSpinner = Spinner[Int](min = 0, max = 59, initialValue = 1, amountToStepBy = 1)
  val amPmSpinner = Spinner[String](items = ampm)

  children = List(hourSpinner, minuteSpinner, amPmSpinner)
  HBox.setHgrow(this, Priority.Always)