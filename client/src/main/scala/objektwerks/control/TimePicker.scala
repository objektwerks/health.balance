package objektwerks.control

import scalafx.scene.control.Spinner
import scalafx.scene.layout.HBox

final class TimePicker extends HBox:
  val hourSpinner = new Spinner[Int]:
    min = 0
    max = 12
    initialValue = 1
    amountToStepBy = 1

  val minuteSpinner = new Spinner[Int]:
    min = 0
    max = 59
    initialValue = 1
    amountToStepBy = 1
  
  