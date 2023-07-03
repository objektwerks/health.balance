package objektwerks.dashboard

import objektwerks.{Context, Model}

final class DrinkablesDashboardPane(context: Context, model: Model) extends DashboardPane(context):
  text = context.tabDrinkables