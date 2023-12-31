package objektwerks.dashboard

import scalafx.geometry.Pos
import scalafx.scene.control.{Label, TitledPane}
import scalafx.scene.layout.{BorderPane, HBox, Priority}

import objektwerks.{Context, Model}

final class MeasurablesDashboardPane(context: Context, model: Model) extends TitledPane:
  alignment = Pos.CENTER
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
    alignment = Pos.CENTER
    collapsible = false
    maxWidth = Double.MaxValue
    maxHeight = Double.MaxValue
    text = context.tabToday
    content = todayControls

  val weekTitledPane = new TitledPane:
    alignment = Pos.CENTER
    collapsible = false
    maxWidth = Double.MaxValue
    maxHeight = Double.MaxValue
    text = context.tabWeek
    content = weekControls

  val dashboard = new BorderPane:
    left = todayTitledPane
    right = weekTitledPane

  content = dashboard