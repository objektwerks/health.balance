package objektwerks.dialog

import scalafx.Includes.*
import scalafx.scene.control.{ButtonType, Dialog}
import scalafx.scene.layout.VBox

import objektwerks.{Client, Context, Model}
import objektwerks.chart.DrinkablesChart

final class DrinkablesChartDialog(context: Context, model: Model) extends Dialog[Unit]:
  initOwner(Client.stage)
  title = context.windowTitle
  headerText = context.tabDrinkables
  
  dialogPane().buttonTypes = List(ButtonType.Close)
  dialogPane().content = new VBox:
    spacing = 6
    children = List( DrinkablesChart(context, model) )