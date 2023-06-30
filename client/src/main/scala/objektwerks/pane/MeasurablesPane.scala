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

  val tableView = new TableView[Measurable]():
    columns ++= List(
      new TableColumn[Measurable, String]:
        text = "Kind"
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Measurable, String]:
        text = "Measurement"
        cellValueFactory = _.value.measurementProperty
      ,
      new TableColumn[Measurable, String]:
        text = "Unit"
        cellValueFactory = _.value.unitProperty
      ,
      new TableColumn[Measurable, String]:
        text = "Measured"
        cellValueFactory = _.value.measuredProperty
    )
    items = model.observableMeasurables