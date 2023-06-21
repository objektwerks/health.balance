package objektwerks

import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.BorderPane

final class View(context: Context, model: Model):
  val borderPane = new BorderPane:
    prefWidth = context.windowWidth
    prefHeight = context.windowHeight
    padding = Insets(6)

  println(model)

  val scene = new Scene:
    root = borderPane
    stylesheets = List("/style.css")