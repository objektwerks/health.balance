package objektwerks.dashboard

import scalafx.geometry.Insets
import scalafx.scene.layout.{HBox, Priority}

import objektwerks.{Context, Model}

final class DashboardPane(context: Context, model: Model) extends HBox:
  spacing = 6
  padding = Insets(6)

  // TODO - panes!
  // val pane = Pane(context, model)
  // HBox.setHgrow(pane, Priority.Always)

  children = List()