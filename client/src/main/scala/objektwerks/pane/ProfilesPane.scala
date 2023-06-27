package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, Tab, TabPane, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Model, Profile}
import objektwerks.dialog.{AccountDialog, DeactivateReactivate, FaultsDialog, ProfileDialog}

class ProfilesPane(context: Context, model: Model) extends VBox:
  spacing = 6
  padding = Insets(6)

  val tableView = new TableView[Profile]():
    columns ++= List(
      new TableColumn[Profile, String]:
        text = "Name"
        cellValueFactory = _.value.nameProperty
      ,
      new TableColumn[Profile, String]:
        text = "Created"
        cellValueFactory = _.value.createdProperty
    )
    items = model.observableProfiles

  tableView.onMouseClicked = { event =>
    if (event.getClickCount == 2 && tableView.selectionModel().getSelectedItem != null) update()
  }

  tableView.selectionModel().selectionModeProperty.value = SelectionMode.Single
  
  tableView.selectionModel().selectedItemProperty().addListener { (_, _, selectedItem) =>
    // model.update executes a remove and add on items. the remove passes a null selectedItem!
    if selectedItem != null then
      model.selectedProfileId.value = selectedItem.id
      editButton.disable = false
  }

  def add(): Unit =
    ProfileDialog(context, Profile(accountId = model.observableAccount.get.id, name = "")).showAndWait() match
      case Some(profile: Profile) =>
        model.add(profile)
        tableView.selectionModel().select(profile)
      case _ =>

  def update(): Unit =
    val selectedIndex = tableView.selectionModel().getSelectedIndex
    val pool = tableView.selectionModel().getSelectedItem.profile
    ProfileDialog(context, pool).showAndWait() match
      case Some(profile: Profile) =>
        model.update(profile)
        tableView.selectionModel().select(selectedIndex)
      case _ =>

  def faults(): Unit = FaultsDialog(context, model).showAndWait() match
    case _ => errorsButton.disable = model.observableFaults.isEmpty

  def account(): Unit = AccountDialog(context, model.observableAccount.get).showAndWait() match
      case Some( DeactivateReactivate( Some(deactivate), None) ) => model.deactivate(deactivate)
      case Some( DeactivateReactivate( None, Some(reactivate) ) ) => model.reactivate(reactivate)
      case _ =>