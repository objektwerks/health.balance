package objektwerks.dashboard

import scalafx.scene.control.TitledPane

import objektwerks.{Context, Model}

abstract class DashboardSummaryPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue