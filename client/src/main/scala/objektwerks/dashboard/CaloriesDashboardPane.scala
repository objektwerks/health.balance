package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}

import objektwerks.Context
import objektwerks.layout.ControlGridPane

abstract class CaloriesDashboardPane(context: Context) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue

  val today = Label("0")
  val week = Label("0")

  val controls = List[(String, Label)](
    context.labelToday -> today,
    context.labelWeek -> week,
  )
  
  content = ControlGridPane(controls)