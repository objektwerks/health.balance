package objektwerks

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

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
      logger.error("*** Register failed. Client stopping ...")
      sys.exit(-1)
    }

    model.loggedin.onChange { (_, _, _) =>
      Alerts.showLoginAlert(context, stage)
      logger.error("*** Login failed. Client stopping ...")
      sys.exit(-1)
    }
    
    RegisterLoginDialog(stage, context).showAndWait() match
      case Some( RegisterLogin( Some(register), None) ) => model.register(register)
      case Some( RegisterLogin( None, Some(login) ) ) => model.login(login)
      case _ =>
    
    stage.show()
    logger.info(s"*** Client started, targeting: ${context.url}")

  override def stopApp(): Unit = logger.info("*** Client stopped.")