package objektwerks.chart

import java.time.format.DateTimeFormatter

import scalafx.Includes.*
import scalafx.geometry.Insets
import scalafx.scene.chart.{LineChart, XYChart}
import scalafx.scene.control.{Tab, TabPane}

import objektwerks.{Context, Entity, Model}

final case class DrinkableXY(xDate: String, yCount: Int)

final class DrinkablesChart(context: Context, model: Model) extends TabPane:
  val drinkables = model.observableDrinkables.reverse
  val dateFormat = DateTimeFormatter.ofPattern("M.dd")
  val minDate = Entity.epochSecondToLocalDate( drinkables.map(d => d.drank).min ).format(dateFormat)
  val maxDate = Entity.epochSecondToLocalDate( drinkables.map(d => d.drank).max ).format(dateFormat)

  val tab = new Tab:
    closable = false
    text = context.tabDrinkables
    content = buildChart()

  padding = Insets(6)
  tabs = List(tab)

  def buildChart(): LineChart[String, Number] =
    val filtered = drinkables.map(e => DrinkableXY( Entity.epochSecondToLocalDate(e.drank).format(dateFormat), e.calories) )
    val (chart, series) = LineChartBuilder.build(context = context,
                                                 xLabel = context.chartMonthDay,
                                                 xMinDate = minDate,
                                                 xMaxDate = maxDate,
                                                 yLabel = context.tabDrinkables,
                                                 yLowerBound = 0,
                                                 yUpperBound = 7,
                                                 yTickUnit = 1,
                                                 yValues = filtered.map(dxy => dxy.yCount))
    filtered foreach { dxy =>
      series.data() += XYChart.Data[String, Number](dxy.xDate.format(dateFormat), dxy.yCount)
    }
    chart.data = series
    LineChartBuilder.addTooltip(chart)
    chart