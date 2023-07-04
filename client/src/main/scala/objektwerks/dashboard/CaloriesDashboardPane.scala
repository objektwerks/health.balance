package objektwerks.dashboard

import scalafx.scene.control.TitledPane

import objektwerks.{Context, Model}

final class CaloriesDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue