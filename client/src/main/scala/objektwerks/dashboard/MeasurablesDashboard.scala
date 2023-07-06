package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}
import scalafx.scene.layout.{HBox, Priority}

import objektwerks.{Context, Model}

final class MeasurablesDashboard(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabMeasurables

  val weight = Label("0")
  val pulse = Label("0")
  val glucose = Label("0")

  val controls = new HBox:
    spacing = 6
    children = List(
      Label(context.labelWeight), weight,
      Label(context.labelPulse), pulse,
      Label(context.labelGlucose), glucose
    )
  HBox.setHgrow(controls, Priority.Always)

  content = controls