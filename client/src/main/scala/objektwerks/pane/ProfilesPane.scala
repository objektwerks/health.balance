package objektwerks.pane

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, SelectionMode, Tab, TabPane, TableColumn, TableView}
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.{Context, Model, Profile}
import objektwerks.dialog.{AccountDialog, FaultsDialog, DeactivateReactivate}

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

  def account(): Unit = AccountDialog(context, model.observableAccount.get).showAndWait() match
      case Some( DeactivateReactivate( Some(deactivate), None) ) => model.deactivate(deactivate)
      case Some( DeactivateReactivate( None, Some(reactivate) ) ) => model.reactivate(reactivate)
      case _ =>