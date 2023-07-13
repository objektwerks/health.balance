package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, DatePicker, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Expendable, ExpendableKind, Entity}
import objektwerks.control.TimePicker
import objektwerks.layout.ControlGridPane

final class ExpendableDialog(context: Context, expendable: Expendable) extends Dialog[Expendable]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogExpendable
  
  val kindComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( ExpendableKind.toList )
  	value = expendable.kind
  kindComboBox.prefWidth = 200

  val detailTextField = new TextField:
    text = expendable.detail

  val sunshineCheckBox = new CheckBox:
    selected = expendable.sunshine

  val freshairCheckBox = new CheckBox:
    selected = expendable.freshair

  val caloriesTextField = new IntTextField:
    text = expendable.calories.toString

  val startDatePicker = DatePicker( Entity.epochSecondToLocalDate(expendable.start) )

  val startTimePicker = TimePicker( Entity.epochSecondToLocalTime(expendable.start) )

  val finishDatePicker = DatePicker( Entity.epochSecondToLocalDate(expendable.finish) )

  val finishTimePicker = TimePicker( Entity.epochSecondToLocalTime(expendable.finish) )

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelDetail -> detailTextField,
    context.labelSunshine -> sunshineCheckBox,
    context.labelFreshair -> freshairCheckBox,
    context.labelCalories -> caloriesTextField,
    context.labelDateStart -> startDatePicker,
    context.labelTimeStart -> startTimePicker,
    context.labelDateFinish -> finishDatePicker,
    context.labelTimeFinish -> finishTimePicker
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      expendable.copy(
        kind = kindComboBox.value.value,
        detail = detailTextField.text.value,
        sunshine = sunshineCheckBox.selected.value,
        freshair = freshairCheckBox.selected.value,
        calories = caloriesTextField.text.value.toIntOption.getOrElse(expendable.calories),
        start = Entity.localDateAndTimeToEpochSecond( startDatePicker.value.value, startTimePicker.value ),
        finish = Entity.localDateAndTimeToEpochSecond( finishDatePicker.value.value, finishTimePicker.value )
      )
    else null
  }