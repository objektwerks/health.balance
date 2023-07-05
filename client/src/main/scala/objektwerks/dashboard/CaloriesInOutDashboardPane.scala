package objektwerks.dashboard

import scalafx.scene.control.TitledPane

import objektwerks.Context

final class CaloriesInOutDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabCaloriesInOut