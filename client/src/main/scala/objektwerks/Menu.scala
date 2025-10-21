package objektwerks

import scalafx.Includes.*
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, Menu => MenuRoot, MenuBar, MenuItem, SeparatorMenuItem, TextArea}
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.layout.GridPane

final class Menu(context: Context) extends MenuBar:
  val textAreaAbout = new TextArea():
    minWidth = 275
    editable = false
    wrapText = true
    text = context.aboutAlertContentText

  val gridPaneAbout = new GridPane():
    prefHeight = 120
    prefWidth = 350
  gridPaneAbout.add(textAreaAbout, 0, 0)

  val menuItemAbout = new MenuItem:
    text = context.menuAbout
    onAction = (_: ActionEvent) =>
      new Alert(AlertType.Information):
        initOwner(Client.stage)
        title = context.windowTitle
        headerText = context.aboutAlertHeaderText
        dialogPane().content = gridPaneAbout
      .showAndWait()

  val menuItemExit = new MenuItem:
    text = context.menuExit
    onAction = (_: ActionEvent) => Platform.exit()

  val menuRoot = new MenuRoot():
    text = context.menuMenu
    items = List(menuItemAbout, SeparatorMenuItem(), menuItemExit)

  menus = List(menuRoot)