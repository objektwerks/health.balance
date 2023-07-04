package objektwerks.dashboard

import scalafx.geometry.Insets
import scalafx.scene.layout.{HBox, Priority}

import objektwerks.{Context, Model}

final class Dashboard(context: Context, model: Model) extends HBox:
  spacing = 6
  padding = Insets(6)

  val ediblesDashboardPane = EdiblesDashboardPane(context, model)
  HBox.setHgrow(ediblesDashboardPane, Priority.Always)

  children = List(ediblesDashboardPane)