package objektwerks.dashboard

import objektwerks.{Context, Model}

// In/Out Ration = Edibles + Drinkables / Expendables
final class CaloriesInOutDashboardPane(context: Context, model: Model) extends CaloriesDashboardPane(context):
  text = context.tabCaloriesInOut
  today.text <== model.caloriesInOutToday
  week.text <== model.caloriesInOutWeek