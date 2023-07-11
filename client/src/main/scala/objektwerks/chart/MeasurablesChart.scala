package objektwerks.chart

import java.time.format.DateTimeFormatter

import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.{Tab, TabPane}

import objektwerks.{Context, Entity, Model}
import objektwerks.MeasurableKind

final class MeasurablesChart(context: Context, model: Model) extends TabPane:
  val measurables = model.observableMeasurables.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = Entity.epochSecondToLocalDate( measurables.map(m => m.measured).min ).format(dateFormat)
  val maxDate = Entity.epochSecondToLocalDate( measurables.map(m => m.measured).max ).format(dateFormat)

  val weightTab = new Tab:
    closable = false
    text = context.tabWeight
    content = buildWeightChart()

  val pulseTab = new Tab:
    closable = false
    text = context.tabPulse
    content = buildPulseChart()

  val glucoseTab = new Tab:
    closable = false
    text = context.tabGlucose
    content = buildGlucoseChart()

  padding = Insets(6)
  tabs = List(weightTab,
               pulseTab,
               glucoseTab)

  private def buildChart(kind: String,
                         yLabel: String,
                         yLowerBound: Int = 0,
                         yUpperBound: Int = 10,
                         yTickUnit: Int = 1): LineChart[String, Number] =
    val filtered = measurables filter(m => m.kind == kind)
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = yLabel,
                                                 yLowerBound = yLowerBound,
                                                 yUpperBound = yUpperBound,
                                                 yTickUnit = yTickUnit,
                                                 yValues = filtered.map(m => m.measurement))
    filtered foreach { m =>
      series.data() += XYChart.Data[String, Number]( Entity.epochSecondToLocalDate(m.measured).format(dateFormat), m.measured)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart

  def buildWeightChart(): LineChart[String, Number] =
    buildChart(kind = MeasurableKind.Weight.toString, yLabel = context.tabWeight)

  def buildPulseChart(): LineChart[String, Number] =
    buildChart(kind = MeasurableKind.Pulse.toString, yLabel = context.tabPulse)

  def buildGlucoseChart(): LineChart[String, Number] =
    buildChart(kind = MeasurableKind.Glucose.toString, yLabel = context.tabGlucose)