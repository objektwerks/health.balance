package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}

import objektwerks.{Context, Model}
import objektwerks.dialog.ControlGridPane

final class CaloriesDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue

  val today = new Label:
    text = "0"
  
  val week = new Label:
    text = "0"

  val controls = List[(String, Label)](
    context.labelToday -> today,
    context.labelWeek -> week,
  )
  
  content = ControlGridPane(controls)