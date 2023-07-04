package objektwerks

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.dashboard.Dashboard
import objektwerks.pane.*

final class View(context: Context, model: Model):
  val dashboardPane = Dashboard(context, model)
  HBox.setHgrow(dashboardPane, Priority.Always)

  val poolsPane = ProfilesPane(context, model)
  VBox.setVgrow(poolsPane, Priority.Always)

  val tabbedPane = TabbedPane(context, model)
  VBox.setVgrow(tabbedPane, Priority.Always)

  val splitPane = new SplitPane {
    orientation = Orientation.Horizontal
    items.addAll(poolsPane, tabbedPane)
  }
  splitPane.setDividerPositions(0.30, 0.70)
  VBox.setVgrow(splitPane, Priority.Always)

  val viewPane = new VBox:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)
    children = List(dashboardPane, splitPane)

  val scene = new Scene:
    root = viewPane
    stylesheets = List("/style.css")