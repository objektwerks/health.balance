package objektwerks.dashboard

import scalafx.scene.control.TitledPane

import objektwerks.{Context, Model}

final class SummaryDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue