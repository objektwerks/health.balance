package objektwerks

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scalafx.application.JFXApp3

object Client extends JFXApp3 with LazyLogging:
  private val conf = ConfigFactory.load("client.conf")
  private val context = Context(conf)
  //private val fetcher = Fetcher(context)
  //private val model = Model(fetcher)

  override def start(): Unit =
    //val view = View(context, model)
    stage = new JFXApp3.PrimaryStage:
      //scene = view.scene
      title = context.windowTitle
      minWidth = context.windowWidth
      minHeight = context.windowHeight
      icons.add(context.logo)

    stage.hide()

  override def stopApp(): Unit = logger.info("*** Client stopped.")