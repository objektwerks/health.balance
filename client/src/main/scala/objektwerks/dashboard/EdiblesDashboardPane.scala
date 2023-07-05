package objektwerks.dashboard

import objektwerks.{Context, Model}

final class EdiblesDashboardPane(context: Context, model: Model) extends CaloriesDashboardPane(context):
  text = context.tabEdibles

  model.observableEdibles.onChange { (_, _) =>
  }