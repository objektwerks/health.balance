package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Drinkable, Model}
import objektwerks.dialog.DrinkableDialog

final class DrinkablesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Drinkable]():
    columns ++= List(
      new TableColumn[Drinkable, String]:
        text = "Kind"
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Drinkable, String]:
        text = "Detail"
        cellValueFactory = _.value.detailProperty
      ,
      new TableColumn[Drinkable, String]:
        text = "Calories"
        cellValueFactory = _.value.caloriesProperty
      ,
      new TableColumn[Drinkable, String]:
        text = "Drank"
        cellValueFactory = _.value.drankProperty
    )
    items = model.observableDrinkables