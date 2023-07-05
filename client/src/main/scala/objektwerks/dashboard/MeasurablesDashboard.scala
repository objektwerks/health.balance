package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}

import objektwerks.{Context, Model}

final class MeasurablesDashboard(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabMeasurables

  val weight = new Label:
    text = "0"

  val pulse = new Label:
    text = "0"

  val glucose = new Label:
    text = "0"

  model.observableMeasurables.onChange { (_, _) =>
  }