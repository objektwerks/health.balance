package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, ComboBox, DatePicker, Dialog}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Measurable, MeasurableKind, Entity}
import objektwerks.layout.ControlGridPane

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

  val measuredDatePicker = DatePicker( Entity.epochSecondToLocalDate(measurable.measured) )

  val controls = List[(String, Region)](
    context.labelKind -> kindComboBox,
    context.labelMeasurement -> measurementTextField,
    context.labelMeasured -> measuredDatePicker
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      measurable.copy(
        kind = kindComboBox.value.value,
        measurement = measurementTextField.text.value.toIntOption.getOrElse(measurable.measurement),
      )
    else null
  }