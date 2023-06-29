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