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
        text = context.headerKind
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Measurable, String]:
        text = context.headerMeasurement
        cellValueFactory = _.value.measurementProperty
      ,
      new TableColumn[Measurable, String]:
        text = context.headerMeasured
        cellValueFactory = _.value.measuredProperty
    )
    items = model.observableMeasurables

  val addButton = new Button:
    graphic = context.addImage
    text = context.buttonAdd
    disable = true
    onAction = { _ => add() }

  val editButton = new Button:
    graphic = context.editImage
    text = context.buttonEdit
    disable = true
    onAction = { _ => update() }

  val chartButton = new Button:
    graphic = context.chartImage
    text = context.buttonChart
    disable = true
    onAction = { _ => chart() }

  val buttonBar = new HBox:
    spacing = 6
    children = List(addButton, editButton, chartButton)

  model.selectedProfileId.onChange { (_, _, _) =>
    addButton.disable = false
  }

  children = List(tableView, buttonBar)
  VBox.setVgrow(tableView, Priority.Always)

  tableView.onMouseClicked = { event =>
    if event.getClickCount == 2 && tableView.selectionModel().getSelectedItem != null then update()
  }

  tableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  tableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedItem) =>
    // model.update executes a remove and add on items. the remove passes a null selectedItem!
    if selectedItem != null then
      model.selectedMeasurableId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    MeasurableDialog(context, Measurable(profileId = model.selectedProfileId.value)).showAndWait() match
      case Some(measurable: Measurable) =>
        model.add(measurable)
        tableView.selectionModel().select(measurable)
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val measurable = tableView.selectionModel().getSelectedItem.measurable
    MeasurableDialog(context, measurable).showAndWait() match
      case Some(measurable: Measurable) =>
        model.update(measurable)
        tableView.selectionModel().select(selectedIndex)
      case _ =>