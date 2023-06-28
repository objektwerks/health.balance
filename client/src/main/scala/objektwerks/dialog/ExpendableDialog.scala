package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, Dialog, Label, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Expendable, ExpendableKind, Entity}

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

  val startLabel = new Label:
    text = Entity.epochSecondToLocalDateTime(expendable.start).toString

  val finishLabel = new Label:
    text = Entity.epochSecondToLocalDateTime(expendable.finish).toString

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelDetail -> detailTextField,
    context.labelSunshine -> sunshineCheckBox,
    context.labelFreshair -> freshairCheckBox,
    context.labelCalories -> caloriesTextField,
    context.labelStart -> startLabel,
    context.labelFinish -> finishLabel
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
        calories = caloriesTextField.text.value.toIntOption.getOrElse(expendable.calories)
      )
    else null
  }