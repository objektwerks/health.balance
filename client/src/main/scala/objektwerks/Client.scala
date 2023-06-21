package objektwerks

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import scalafx.application.JFXApp3

object Client extends JFXApp3 with LazyLogging:
  private val conf = ConfigFactory.load("client.conf")
  println(conf)

  override def start(): Unit = logger.info("*** Client stopped.")

  override def stopApp(): Unit = logger.info("*** Client stopped.")