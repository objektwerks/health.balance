package objektwerks.chart

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.{Tab, TabPane}

import objektwerks.{Context, Model}

final case class EdibleXY(xDate: String, yCount: Int)

final class EdiblesChart(context: Context, model: Model) extends TabPane:
  val edibles = model.observableEdibles.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = LocalDate.ofEpochDay( edibles.map(e => e.ate).min).format(dateFormat)
  val maxDate = LocalDate.ofEpochDay( edibles.map(e => e.ate).max).format(dateFormat)

  val tab = new Tab:
    closable = false
    text = context.chartYEdibles
    content = buildChart()

  padding = Insets(6)
  tabs = List(tab)

  def buildChart(): LineChart[String, Number] =
    val filtered = edibles.map(e => EdibleXY( LocalDate.ofEpochDay(e.ate).format(dateFormat), e.calories))
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = context.chartYEdibles,
                                                 yLowerBound = 0,
                                                 yUpperBound = 7,
                                                 yTickUnit = 1,
                                                 yValues = filtered.map(cxy => cxy.yCount))
    filtered foreach { cxy =>
      series.data() += XYChart.Data[String, Number](cxy.xDate.format(dateFormat), cxy.yCount)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart