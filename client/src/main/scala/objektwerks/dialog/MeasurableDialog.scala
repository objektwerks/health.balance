package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, CheckBox, ComboBox, Dialog, Label, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Measurable, MeasurableKind, Entity, UnitOfMeasure}

final class MeasurableDialog(context: Context, measurable: Measurable) extends Dialog[Measurable]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogMeasurable
  
  val kindComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( MeasurableKind.toList )
  	value = measurable.kind
  kindComboBox.prefWidth = 200

  val measurementTextField = new IntTextField:
    text = measurable.measurement.toString

  val unitComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( UnitOfMeasure.toList )
  	value = measurable.unit

  val measuredLabel = new Label:
    text = Entity.epochSecondToLocalDateTime(measurable.measured).toString

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelMeasurement -> measurementTextField,
    context.labelUnit -> unitCheckBox,
    context.labelMeasured -> measuredLabel
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      measurable.copy(
        kind = kindComboBox.value.value,
        measurement = measurementTextField.text.value.toIntOption.getOrElse(measurable.measurement),
        unit = unitComboBox.value.value
      )
    else null
  }