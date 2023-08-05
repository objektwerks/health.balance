package objektwerks.pane

import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Expendable, Model}
import objektwerks.dialog.{ExpendableDialog, ExpendablesChartDialog}

final class ExpendablesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val yesOrNo = (bool: Boolean) => if bool then context.columnYes else context.columnNo

  val tableView = new TableView[Expendable]():
    columns ++= List(
      new TableColumn[Expendable, String]:
        text = context.headerKind
        cellValueFactory = _.value.kindProperty
      ,
      new TableColumn[Expendable, String]:
        text = context.headerDetail
        cellValueFactory = _.value.detailProperty
      ,
      new TableColumn[Expendable, Boolean]:
        text = context.headerSunshine
        cellValueFactory = _.value.sunshineProperty
        cellFactory = (cell, bool) => cell.text = yesOrNo(bool)
      ,
      new TableColumn[Expendable, Boolean]:
        text = context.headerFreshair
        cellValueFactory = _.value.freshairProperty
        cellFactory = (cell, bool) => cell.text = yesOrNo(bool)
      ,
      new TableColumn[Expendable, String]:
        text = context.headerCalories
        cellValueFactory = _.value.caloriesProperty
      ,
      new TableColumn[Expendable, String]:
        text = context.headerStart
        cellValueFactory = _.value.startProperty
        prefWidth = 150
      ,
      new TableColumn[Expendable, String]:
        text = context.headerFinish
        cellValueFactory = _.value.finishProperty
        prefWidth = 150
    )
    items = model.observableExpendables

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
      model.selectedExpendableId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    ExpendableDialog(context, Expendable(profileId = model.selectedProfileId.value)).showAndWait() match
      case Some(expendable: Expendable) =>
        model.add(expendable){ tableView.selectionModel().select(0) }
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val expendable = tableView.selectionModel().getSelectedItem.expendable
    ExpendableDialog(context, expendable).showAndWait() match
      case Some(expendable: Expendable) =>
        model.update(selectedIndex, expendable)
        Platform.runLater(tableView.selectionModel().select(selectedIndex))
      case _ =>

  def chart(): Unit = ExpendablesChartDialog(context, model).showAndWait()