package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, DatePicker, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Edible, EdibleKind, Entity}
import objektwerks.control.TimePicker
import objektwerks.layout.ControlGridPane
import objektwerks.control.IntTextField

final class EdibleDialog(context: Context, edible: Edible) extends Dialog[Edible]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogEdible
  
  val kindComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( EdibleKind.toList )
  	value = edible.kind
  kindComboBox.prefWidth = 200

  val detailTextField = new TextField:
    text = edible.detail

  val organicCheckBox = new CheckBox:
    selected = edible.organic

  val caloriesTextField = new IntTextField:
    text = edible.calories.toString

  val ateDatePicker = DatePicker( Entity.epochSecondToLocalDate(edible.ate) )

  val ateTimePicker = TimePicker( Entity.epochSecondToLocalTime(edible.ate) )

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelDetail -> detailTextField,
    context.labelOrganic -> organicCheckBox,
    context.labelCalories -> caloriesTextField,
    context.labelDateAte -> ateDatePicker,
    context.labelTimeAte -> ateTimePicker
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      edible.copy(
        kind = kindComboBox.value.value,
        detail = detailTextField.text.value,
        organic = organicCheckBox.selected.value,
        calories = caloriesTextField.text.value.toIntOption.getOrElse(edible.calories),
        ate = Entity.localDateAndTimeToEpochSecond( ateDatePicker.value.value, ateTimePicker.time )
      )
    else null
  }