package objektwerks.dashboard

import objektwerks.{Context, Model}

final class EdiblesDashboardPane(context: Context, model: Model) extends DashboardPane(context):
  text = context.tabEdibles