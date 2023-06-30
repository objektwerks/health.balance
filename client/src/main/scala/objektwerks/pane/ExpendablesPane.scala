package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Expendable, Model}
import objektwerks.dialog.ExpendableDialog

final class ExpendablesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Expendable]():
    columns ++= List(
      new TableColumn[Expendable, String]:
        text = "Kind"
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Expendable, String]:
        text = "Detail"
        cellValueFactory = _.value.detailProperty
      ,
      new TableColumn[Expendable, Boolean]:
        text = "Sunshine"
        cellValueFactory = _.value.sunshineProperty
        cellFactory = (cell, bool) => cell.text = yesOrNo(bool)
      ,
      new TableColumn[Expendable, Boolean]:
        text = "Freshair"
        cellValueFactory = _.value.freshairProperty
        cellFactory = (cell, bool) => cell.text = yesOrNo(bool)
      ,
      new TableColumn[Expendable, String]:
        text = "Calories"
        cellValueFactory = _.value.caloriesProperty
      ,
      new TableColumn[Expendable, String]:
        text = "Start"
        cellValueFactory = _.value.startProperty
      ,
      new TableColumn[Expendable, String]:
        text = "Finish"
        cellValueFactory = _.value.finishProperty
    )
    items = model.observableExpendables