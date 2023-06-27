package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, ComboBox, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Edible, EdibleKind}
import scalafx.collections.ObservableBuffer

final class EdibleDialog(context: Context, edible: Edible) extends Dialog[Edible]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogProfile
  
  val kindComboBox = new ComboBox[String]:
  	items = ObservableBuffer.from( EdibleKind.toList )
  	value = edible.kind.toString
  kindComboBox.prefWidth = 200

  val detailTextField = new TextField:
    text = edible.detail

  val caloriesTextField = new IntTextField:
    text = pool.volume.toString

  val controls = List[(String, Region)](
    context.labelDetail -> detailTextField
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      edible.copy(
        detail = detailTextField.text.value
      )
    else null
  }