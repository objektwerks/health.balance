package objektwerks.dashboard

import scalafx.scene.control.TitledPane

import objektwerks.Context

abstract class DashboardTitledPane(context: Context) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue