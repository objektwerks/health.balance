package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, Dialog, Label, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Edible, EdibleKind, Entity}

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

  val ateLabel = new Label:
    text = Entity.epochSecondToLocalDateTime(edible.ate).toString

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelDetail -> detailTextField,
    context.labelOrganic -> organicCheckBox,
    context.labelCalories -> caloriesTextField
    context.labelAte -> ateLabel
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
        calories = caloriesTextField.text.value.toIntOption.getOrElse(edible.calories)
      )
    else null
  }