package objektwerks.dashboard

import objektwerks.{Context, Model}

final class MeasurablesDashboardPane(context: Context, model: Model) extends DashboardPane(context):
   text = context.tabMeasurables