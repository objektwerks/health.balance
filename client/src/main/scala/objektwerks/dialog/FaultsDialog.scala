package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.control.{ButtonType, Dialog, TableColumn, TableView}
import scalafx.scene.layout.VBox

import objektwerks.{Client, Context, Fault, Model}

final class FaultsDialog(context: Context, model: Model) extends Dialog[Unit]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.dialogFaults

  val tableView = new TableView[Fault]():
    columns ++= List(
      new TableColumn[Fault, Long]:
        text = context.headerOccurred
        cellValueFactory = _.value.occurredProperty
      ,
      new TableColumn[Fault, String]:
        prefWidth = 425
        text = context.headerFault
        cellValueFactory = _.value.causeProperty
    )
    items = model.observableFaults

  dialogPane().buttonTypes = List(ButtonType.Close)
  dialogPane().content = new VBox:
    prefWidth = 600
    prefHeight = 200
    spacing = 6
    children = List(tableView)