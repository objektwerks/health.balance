package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Profile}
import objektwerks.layout.ControlGridPane

final class ProfileDialog(context: Context, profile: Profile) extends Dialog[Profile]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogProfile

  val nameTextField = new TextField:
    text = profile.name

  val controls = List[(String, Region)](
    context.labelName -> nameTextField
  )
  dialogPane().content = ControlGridPane(controls)

  val saveButtonType = new ButtonType(context.buttonSave, ButtonData.OKDone)
  dialogPane().buttonTypes = List(saveButtonType, ButtonType.Cancel)

  resultConverter = dialogButton => {
    if dialogButton == saveButtonType then
      profile.copy(
        name = nameTextField.text.value
      )
    else null
  }