package objektwerks.dashboard

import scalafx.scene.control.TitledPane

import objektwerks.{Context, Model}

final class MeasurablesDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue

  text = context.tabMeasurables