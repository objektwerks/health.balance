package objektwerks.dashboard

import scalafx.geometry.Insets
import scalafx.scene.layout.{HBox, Priority}

import objektwerks.{Context, Model}

final class CaloriesDashboard(context: Context, model: Model) extends HBox:
  spacing = 6
  padding = Insets(6)

  val ediblesDashboardPane = EdiblesDashboardPane(context, model)
  HBox.setHgrow(ediblesDashboardPane, Priority.Always)

  val drinkablesDashboardPane = DrinkablesDashboardPane(context, model)
  HBox.setHgrow(drinkablesDashboardPane, Priority.Always)

  val expendablesDashboardPane = EdiblesDashboardPane(context, model)
  HBox.setHgrow(expendablesDashboardPane, Priority.Always)

  val caloriesDashboardPane = CaloriesDashboardPane(context, model)
  HBox.setHgrow(caloriesDashboardPane, Priority.Always)

  children = List(ediblesDashboardPane,
                  drinkablesDashboardPane,
                  expendablesDashboardPane,
                  caloriesDashboardPane)