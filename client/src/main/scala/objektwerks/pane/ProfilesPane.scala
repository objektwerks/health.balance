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