package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}
import scalafx.scene.layout.{HBox, Priority}

import objektwerks.{Context, Model}

final class MeasurablesDashboard(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabMeasurables

  val weightToday = Label("0")
  val pulseToday = Label("0")
  val glucoseToday = Label("0")

  val todayControls = new HBox:
    spacing = 6
    children = List(
      Label(context.labelWeight), weightToday,
      Label(context.labelPulse), pulseToday,
      Label(context.labelGlucose), glucoseToday
    )
  HBox.setHgrow(todayControls, Priority.Always)

  val todayTitledPane = new TitledPane:
    text = context.tabToday
    content = todayControls


  content = todayTitledPane