package objektwerks.dashboard

import objektwerks.{Context, Model}

final class ExpendablesDashboardPane(context: Context, model: Model) extends DashboardTitledPane(context):
   text = context.tabExpendables 