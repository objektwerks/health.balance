package objektwerks.dashboard

import objektwerks.{Context, Model}

final class ExpendablesDashboardPane(context: Context, model: Model) extends DashboardPane(context):
   text = context.tabExpendables