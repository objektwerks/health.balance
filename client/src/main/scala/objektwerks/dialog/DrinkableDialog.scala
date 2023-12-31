package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, DatePicker, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Drinkable, DrinkableKind, Entity}
import objektwerks.control.TimePicker
import objektwerks.layout.ControlGridPane

final class DrinkableDialog(context: Context, drinkable: Drinkable) extends Dialog[Drinkable]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogDrinkable
  
  val kindComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( DrinkableKind.toList )
  	value = drinkable.kind
  kindComboBox.prefWidth = 200

  val detailTextField = new TextField:
    text = drinkable.detail

  val organicCheckBox = new CheckBox:
    selected = drinkable.organic

  val countTextField = new IntTextField:
    text = drinkable.count.toString

  val caloriesTextField = new IntTextField:
    text = drinkable.calories.toString

  val drankDatePicker = DatePicker( Entity.epochSecondToLocalDate(drinkable.drank) )

  val drankTimePicker = TimePicker( Entity.epochSecondToLocalTime(drinkable.drank) )

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelDetail -> detailTextField,
    context.labelOrganic -> organicCheckBox,
    context.labelCount -> countTextField,
    context.labelCalories -> caloriesTextField,
    context.labelDateDrank -> drankDatePicker,
    context.labelTimeDrank -> drankTimePicker
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      drinkable.copy(
        kind = kindComboBox.value.value,
        detail = detailTextField.text.value,
        organic = organicCheckBox.selected.value,
        calories = caloriesTextField.text.value.toIntOption.getOrElse(drinkable.calories),
        drank = Entity.localDateAndTimeToEpochSecond( drankDatePicker.value.value, drankTimePicker.time )
      )
    else null
  }