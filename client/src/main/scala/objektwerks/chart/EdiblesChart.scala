package objektwerks.chart

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.{Tab, TabPane}

import objektwerks.{Context, Entity, Model}

final case class EdibleXY(xDate: String, yCount: Int)

final class EdiblesChart(context: Context, model: Model) extends TabPane:
  val edibles = model.observableEdibles.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = Entity.epochSecondToLocalDate( edibles.map(e => e.ate).min ).format(dateFormat)
  val maxDate = Entity.epochSecondToLocalDate( edibles.map(e => e.ate).max ).format(dateFormat)

  val tab = new Tab:
    closable = false
    text = context.tabEdibles
    content = buildChart()

  padding = Insets(6)
  tabs = List(tab)

  def buildChart(): LineChart[String, Number] =
    val filtered = edibles.map(e => EdibleXY( Entity.epochSecondToLocalDate(e.ate).format(dateFormat), e.calories) )
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = context.headerCalories,
                                                 yLowerBound = 0,
                                                 yUpperBound = 7,
                                                 yTickUnit = 1,
                                                 yValues = filtered.map(exy => exy.yCount))
    filtered foreach { exy =>
      series.data() += XYChart.Data[String, Number](exy.xDate.format(dateFormat), exy.yCount)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart