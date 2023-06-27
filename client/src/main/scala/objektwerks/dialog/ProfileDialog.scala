package objektwerks.dialog

import scalafx.Includes.*
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout.Region
import scalafx.scene.control.{ButtonType, ComboBox, Dialog, TextField}
import scalafx.scene.control.ButtonBar.ButtonData

import objektwerks.{Client, Context, Profile}

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