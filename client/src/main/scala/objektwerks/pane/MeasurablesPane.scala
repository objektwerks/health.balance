package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Measurable, Model}
import objektwerks.dialog.MeasurableDialog

final class MeasurablesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)