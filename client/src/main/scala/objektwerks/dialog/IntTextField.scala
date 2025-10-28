package objektwerks.dialog

import scalafx.scene.control.{TextField, TextFormatter}
import scalafx.scene.control.TextFormatter.Change
import scalafx.util.converter.IntStringConverter

object IntTextField:
  val regex = """[0-9]+""".r

class IntTextField extends TextField:
  val converter = IntStringConverter()

  val filter: Change => Change = { (change: Change) =>
    if IntTextField.regex.matches(change.controlNewText) then change
    else null
  }

  textFormatter = TextFormatter[Int](converter, 0, filter)

  def int(default: Int): Int = text.value.toIntOption.getOrElse(default)