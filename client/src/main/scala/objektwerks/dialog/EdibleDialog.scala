package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Edible}

final class EdibleDialog(context: Context, edible: Edible) extends Dialog[Edible]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogProfile

  val detailTextField = new TextField:
    text = edible.detail

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