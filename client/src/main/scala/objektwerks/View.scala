package objektwerks

import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{HBox, Priority, VBox}

import objektwerks.dashboard.CaloriesDashboard
import objektwerks.pane.*

final class View(context: Context, model: Model):
  val caloriesDashboardPane = CaloriesDashboard(context, model)
  HBox.setHgrow(caloriesDashboardPane, Priority.Always)

  val profilesPane = ProfilesPane(context, model)
  VBox.setVgrow(profilesPane, Priority.Always)

  val tabbedPane = TabbedPane(context, model)
  VBox.setVgrow(tabbedPane, Priority.Always)

  val splitPane = new SplitPane {
    orientation = Orientation.Horizontal
    items.addAll(profilesPane, tabbedPane)
  }
  splitPane.setDividerPositions(0.40, 0.60)
  VBox.setVgrow(splitPane, Priority.Always)

  val viewPane = new VBox:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)
    children = List(caloriesDashboardPane, splitPane)

  val scene = new Scene:
    root = viewPane
    stylesheets = List("/style.css")