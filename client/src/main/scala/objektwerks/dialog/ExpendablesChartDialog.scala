package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

import objektwerks.{Client, Context, Model}
import objektwerks.chart.ExpendablesChart

final class ExpendablesChartDialog(context: Context, model: Model) extends Dialog[Unit]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.tabExpendables
  
  dialogPane().buttonTypes = List(ButtonType.Close)
  dialogPane().content = new VBox:
    spacing = 6
    children = List( ExpendablesChart(context, model) )