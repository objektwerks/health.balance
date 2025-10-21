package objektwerks

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.dashboard.Dashboard
import objektwerks.pane.*

final class View(context: Context, model: Model):
  val menu = Menu(context)

  val dashboard = Dashboard(context, model)
  HBox.setHgrow(dashboard, Priority.Always)

  val profilesPane = ProfilesPane(context, model)
  VBox.setVgrow(profilesPane, Priority.Always)

  val tabbedPane = TabbedPane(context, model)
  VBox.setVgrow(tabbedPane, Priority.Always)

  val splitPane = new SplitPane {
    orientation = Orientation.Horizontal
    items.addAll(profilesPane, tabbedPane)
  }
  splitPane.setDividerPositions(0.35, 0.65)
  VBox.setVgrow(splitPane, Priority.Always)

  val viewPane = new VBox:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)
    children = List(menu, dashboard, splitPane)

  val scene = new Scene:
    root = viewPane
    stylesheets = List("/style.css")