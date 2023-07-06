package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}
import scalafx.scene.layout.{HBox, Priority}

import objektwerks.{Context, Model}

final class MeasurablesDashboard(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabMeasurables

  val weightToday = new Label:
    text <== model.weightToday

  val pulseToday = new Label:
    text <== model.pulseToday

  val glucoseToday = new Label:
    text <== model.glucoseToday

  val weightWeek = new Label:
    text <== model.weightWeek

  val pulseWeek = new Label:
    text <== model.pulseWeek

  val glucoseWeek = new Label:
    text <== model.glucoseWeek

  val todayControls = new HBox:
    spacing = 6
    children = List(
      Label(context.labelWeight), weightToday,
      Label(context.labelPulse), pulseToday,
      Label(context.labelGlucose), glucoseToday
    )
  HBox.setHgrow(todayControls, Priority.Always)

  val weekControls = new HBox:
    spacing = 6
    children = List(
      Label(context.labelWeight), weightWeek,
      Label(context.labelPulse), pulseWeek,
      Label(context.labelGlucose), glucoseWeek
    )
  HBox.setHgrow(weekControls, Priority.Always)

  val todayTitledPane = new TitledPane:
    collapsible = false
    maxWidth = Double.MaxValue
    maxHeight = Double.MaxValue
    text = context.tabToday
    content = todayControls

  content = todayTitledPane