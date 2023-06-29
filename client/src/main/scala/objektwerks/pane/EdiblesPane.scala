package objektwerks.pane

import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Edible, Model}

final class EdiblesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Edible]():
    columns ++= List(
      new TableColumn[Edible, String]:
        text = "Kind"
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Edible, String]:
        text = "Detail"
        cellValueFactory = _.value.detailProperty
      ,
      new TableColumn[Edible, String]:
        text = "Calories"
        cellValueFactory = _.value.caloriesProperty
      ,
      new TableColumn[Edible, String]:
        text = "Ate"
        cellValueFactory = _.value.ateProperty
    )
    items = model.observableEdibles