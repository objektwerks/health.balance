package objektwerks

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import java.awt.{Taskbar, Toolkit}
import java.awt.Taskbar.Feature

import scalafx.application.JFXApp3

import dialog.{Alerts, RegisterLogin, RegisterLoginDialog}

object Client extends JFXApp3 with LazyLogging:
  private val conf = ConfigFactory.load("client.conf")
  private val context = Context(conf)
  private val fetcher = Fetcher(context)
  private val model = Model(fetcher)

  override def start(): Unit =
    val view = View(context, model)
    stage = new JFXApp3.PrimaryStage:
      scene = view.scene
      title = context.windowTitle
      minWidth = context.windowWidth
      minHeight = context.windowHeight
      icons.add(context.logo)

    stage.hide()

    model.registered.onChange { (_, _, _) =>
      Alerts.showRegisterAlert(context, stage)
      logger.error("*** register failed, client stopping ...")
      sys.exit(-1)
    }

    model.loggedin.onChange { (_, _, _) =>
      Alerts.showLoginAlert(context, stage)
      logger.error("*** login failed, client stopping ...")
      sys.exit(-1)
    }
    
    RegisterLoginDialog(stage, context).showAndWait() match
      case Some( RegisterLogin( Some(register), None) ) => model.register(register)
      case Some( RegisterLogin( None, Some(login) ) ) => model.login(login)
      case _ =>
    
    stage.show()
    logger.info("*** client started, server url: {} endpoint: {}", context.url, context.endpoint)

  override def stopApp(): Unit = logger.info("*** client stopped.")