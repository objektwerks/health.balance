package objektwerks.pane

import scalafx.geometry.Insets
import scalafx.scene.control.{Tab, TabPane}
import scalafx.scene.layout.{Priority, VBox}

import objektwerks.{Context, Model}

final class TabbedPane(context: Context, model: Model) extends VBox:
  padding = Insets(6)

  val ediblesTab = new Tab:
    text = context.tabCleanings
    closable = false
    content = EdiblesPane(context, model)

  val drinkablesTab = new Tab:
  	text = context.tabMeasurements
  	closable = false
  	content = DrinkablesPane(context, model)

  val expendablesTab = new Tab:
  	text = context.tabChemicals
  	closable = false
  	content = ExpendablesPane(context, model)

  val measurablesTab = new Tab:
  	text = context.tabChemicals
  	closable = false
  	content = MeasurablesPane(context, model)

  val tabPane = new TabPane:
    tabs = List(ediblesTab, drinkablesTab, expendablesTab, measurablesTab)

  children = List(tabPane)
  VBox.setVgrow(tabPane, Priority.Always)