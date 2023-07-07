package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, Tab, TabPane, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Model, Profile}
import objektwerks.dialog.{AccountDialog, DeactivateReactivate, FaultsDialog, ProfileDialog}

final class ProfilesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Profile]():
    columns ++= List(
      new TableColumn[Profile, String]:
        text = context.headerName
        cellValueFactory = _.value.nameProperty
      ,
      new TableColumn[Profile, String]:
        text = context.headerCreated
        cellValueFactory = _.value.createdProperty
    )
    items = model.observableProfiles

  val addButton = new Button:
    graphic = context.addImage
    text = context.buttonAdd
    disable = false
    onAction = { _ => add() }

  val editButton = new Button:
    graphic = context.editImage
    text = context.buttonEdit
    disable = true
    onAction = { _ => update() }

  val faultsButton = new Button:
    graphic = context.faultsImage
    text = context.buttonFaults
    disable = true
    onAction = { _ => faults() }

  val accountButton = new Button:
    graphic = context.accountImage
    text = context.buttonAccount
    disable = false
    onAction = { _ => account() }

  val buttonBar = new HBox:
    spacing = 6
    children = List(addButton, editButton, faultsButton, accountButton)

  val tab = new Tab:
  	text = context.tabProfiles
  	closable = false
  	content = new VBox {
      spacing = 6
      padding = Insets(6)
      children = List(tableView, buttonBar)
    }

  val tabPane = new TabPane:
    tabs = List(tab)

  children = List(tabPane)
  VBox.setVgrow(tableView, Priority.Always)
  VBox.setVgrow(tabPane, Priority.Always)

  model.observableFaults.onChange { (_, _) =>
    faultsButton.disable = false
  }

  tableView.onMouseClicked = { event =>
    if event.getClickCount == 2 && tableView.selectionModel().getSelectedItem != null then update()
  }

  tableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  
  tableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedItem) =>
    // model.update executes a remove and add on items. the remove passes a null selectedItem!
    if selectedItem != null then
      model.selectedProfileId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    ProfileDialog(context, Profile(accountId = model.objectAccount.get.id, name = "")).showAndWait() match
      case Some(profile: Profile) =>
        model.add(profile)
        tableView.selectionModel().select(profile)
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val profile = tableView.selectionModel().getSelectedItem.profile
    ProfileDialog(context, profile).showAndWait() match
      case Some(profile: Profile) =>
        model.update(profile)
        tableView.selectionModel().select(selectedIndex)
      case _ =>

  def faults(): Unit = FaultsDialog(context, model).showAndWait() match
    case _ => faultsButton.disable = model.observableFaults.isEmpty

  def account(): Unit = AccountDialog(context, model.objectAccount.get).showAndWait() match
      case Some( DeactivateReactivate( Some(deactivate), None) ) => model.deactivate(deactivate)
      case Some( DeactivateReactivate( None, Some(reactivate) ) ) => model.reactivate(reactivate)
      case _ =>