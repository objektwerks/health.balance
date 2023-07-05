package objektwerks.dashboard

import scalafx.geometry.Insets
import scalafx.scene.control.{Label, TitledPane}
import scalafx.scene.layout.HBox

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

  val controls = new HBox:
    padding = Insets(6)
    children = List(context.labelWeight, weight, context.labelPulse, pulse, context.labelGlucose, glucose)

  content = controls

  model.observableMeasurables.onChange { (_, _) =>
  }