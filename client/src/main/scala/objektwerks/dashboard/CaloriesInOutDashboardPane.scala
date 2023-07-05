package objektwerks.dashboard

import objektwerks.{Context, Model}

// In/Out Ration = Edibles + Drinkables / Expendables
final class CaloriesInOutDashboardPane(context: Context, model: Model) extends CaloriesDashboardPane(context):
  text = context.tabCaloriesInOut
  today.text = "0/0"
  week.text = "0/0"
  
  model.observableEdibles.onChange { (_, _) =>
  }

  model.observableDrinkables.onChange { (_, _) =>
  }

  model.observableExpendables.onChange { (_, _) =>
  }