package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Edible, Model}
import objektwerks.dialog.EdibleDialog

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

  val buttonBar = new HBox:
    spacing = 6
    children = List(addButton, editButton)

  model.selectedProfileId.onChange { (_, _, _) =>
    addButton.disable = false
  }

  children = List(tableView, buttonBar)
  VBox.setVgrow(tableView, Priority.Always)

  tableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 && tableView.selectionModel().getSelectedItem != null) update()
  }

  tableView.selectionModel().selectionModeProperty.value = SelectionMode.Single

  tableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedItem) =>
    // model.update executes a remove and add on items. the remove passes a null selectedItem!
    if selectedItem != null then
      model.selectedEdibleId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    EdibleDialog(context, Edible(profileId = model.selectedProfileId.value)).showAndWait() match
      case Some(edible: Edible) =>
        model.add(edible)
        tableView.selectionModel().select(edible)
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val edible = tableView.selectionModel().getSelectedItem.edible
    EdibleDialog(context, edible).showAndWait() match
      case Some(edible: Edible) =>
        model.update(edible)
        tableView.selectionModel().select(selectedIndex)
      case _ =>