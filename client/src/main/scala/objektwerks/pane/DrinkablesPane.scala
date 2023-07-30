package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Drinkable, Model}
import objektwerks.dialog.{DrinkableDialog, DrinkablesChartDialog}

final class DrinkablesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val yesOrNo = (bool: Boolean) => if bool then context.columnYes else context.columnNo

  val tableView = new TableView[Drinkable]():
    columns ++= List(
      new TableColumn[Drinkable, String]:
        text = context.headerKind
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Drinkable, String]:
        text = context.headerDetail
        cellValueFactory = _.value.detailProperty
      ,
      new TableColumn[Drinkable, Boolean]:
        text = context.headerOrganic
        cellValueFactory = _.value.organicProperty
        cellFactory = (cell, bool) => cell.text = yesOrNo(bool)
      ,
      new TableColumn[Drinkable, String]:
        text = context.headerCount
        cellValueFactory = _.value.countProperty
      ,
      new TableColumn[Drinkable, String]:
        text = context.headerCalories
        cellValueFactory = _.value.caloriesProperty
      ,
      new TableColumn[Drinkable, String]:
        text = context.headerDrank
        cellValueFactory = _.value.drankProperty
    )
    items = model.observableDrinkables

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
    chartButton.disable = false
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
      model.selectedDrinkableId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    DrinkableDialog(context, Drinkable(profileId = model.selectedProfileId.value)).showAndWait() match
      case Some(drinkable: Drinkable) =>
        model.add(drinkable)
        tableView.selectionModel().select(drinkable)
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val drinkable = tableView.selectionModel().getSelectedItem.drinkable
    DrinkableDialog(context, drinkable).showAndWait() match
      case Some(drinkable: Drinkable) =>
        model.update(drinkable)
        tableView.selectionModel().select(selectedIndex)
      case _ =>

  def chart(): Unit = DrinkablesChartDialog(context, model).showAndWait()