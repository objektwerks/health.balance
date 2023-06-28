package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, Dialog, Label, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Drinkable, DrinkableKind, Entity}

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

  val caloriesTextField = new IntTextField:
    text = drinkable.calories.toString

  val drankLabel = new Label:
    text = Entity.epochSecondToLocalDateTime(drinkable.drank).toString

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelDetail -> detailTextField,
    context.labelOrganic -> organicCheckBox,
    context.labelCalories -> caloriesTextField,
    context.labelDrank -> drankLabel
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
        calories = caloriesTextField.text.value.toIntOption.getOrElse(drinkable.calories)
      )
    else null
  }