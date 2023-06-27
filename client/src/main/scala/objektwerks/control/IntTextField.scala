package objektwerks.control

import scalafx.scene.control.{TextField, TextFormatter}
import scalafx.scene.control.TextFormatter.Change
import scalafx.util.converter.IntStringConverter

object IntTextField:
  val regex = """[0-9]+""".r

class IntTextField extends TextField:
  val converter = IntStringConverter()
  val filter: Change => Change = { (change: Change) =>
    if IntTextField.regex.matches(change.text) then
      change // if integer, make change
    else
      change.setText("") // else make no change
      change
  }
  val formatter = new TextFormatter[Int](converter, 0, filter)

  textFormatter = formatter