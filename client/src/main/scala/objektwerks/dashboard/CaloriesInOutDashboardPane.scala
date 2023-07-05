package objektwerks.dashboard

import scalafx.scene.control.{Label, TitledPane}

import objektwerks.{Context, Model}
import objektwerks.dialog.ControlGridPane

final class CaloriesInOutDashboardPane(context: Context, model: Model) extends TitledPane:
  collapsible = false
  maxWidth = Double.MaxValue
  maxHeight = Double.MaxValue
  text = context.tabCaloriesInOut

  val in = new Label:
    text = "0"
  
  val out = new Label:
    text = "0"

  val controls = List[(String, Label)](
    context.labelIn -> in,
    context.labelOut -> out,
  )
  
  content = ControlGridPane(controls)

  model.observableEdibles.onChange { (_, _) =>
  }

  model.observableDrinkables.onChange { (_, _) =>
  }

  model.observableExpendables.onChange { (_, _) =>
  }