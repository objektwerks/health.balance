package objektwerks.dashboard

import objektwerks.{Context, Model}

final class DrinkablesDashboardPane(context: Context, model: Model) extends CaloriesDashboardPane(context):
  text = context.tabDrinkables
  today.text <== model.drinkablesTodayCalories
  week.text <== model.drinkablesWeekCalories