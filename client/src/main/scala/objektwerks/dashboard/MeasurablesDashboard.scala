package objektwerks.dashboard

import objektwerks.{Context, Model}

final class MeasurablesDashboard(context: Context, model: Model) extends DashboardPane(context):
   text = context.tabMeasurables