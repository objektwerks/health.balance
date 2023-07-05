package objektwerks.dashboard

import objektwerks.{Context, Model}

final class ExpendablesDashboardPane(context: Context, model: Model) extends CaloriesDashboardPane(context):
  text = context.tabExpendables

  model.observableExpendables.onChange { (_, _) =>
  }