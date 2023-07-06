package objektwerks.dashboard

import objektwerks.{Context, Model}

final class ExpendablesDashboardPane(context: Context, model: Model) extends CaloriesDashboardPane(context):
  text = context.tabExpendables
  today.text <== model.expendablesTodayCalories
  week.text <== model.expendablesWeekCalories