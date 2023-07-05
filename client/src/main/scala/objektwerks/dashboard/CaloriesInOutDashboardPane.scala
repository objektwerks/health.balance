package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}

import objektwerks.Context

final class CaloriesInOutDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabCaloriesInOut

  val in = new Label:
    text = "0"
  
  val out = new Label:
    text = "0"