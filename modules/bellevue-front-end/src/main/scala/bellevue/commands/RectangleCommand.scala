package bellevue.commands

import bellevue.domain.geometry.Pixels.px
import bellevue.domain.geometry.Rectangle
import bellevue.html.BellevueHtml
import cats.effect.IO
import cats.syntax.show.*
import org.scalajs.dom
import tyrian.Cmd

trait RectangleCommand:

  def drawRectangle(rectangle: Rectangle): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      DrawingCanvas.get.context2d.strokeRect(
        x = rectangle.topLeft.x,
        y = rectangle.topLeft.y,
        w = rectangle.width,
        h = rectangle.height
      )

  val showOverlaidRectange: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangle = getOverlaidRectangle
      rectangle.style.visibility = "visible"
      rectangle.style.borderWidth = 1.px.show

  def drawOverlaidRectangle(rectangle: Rectangle): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangleElem = getOverlaidRectangle
      rectangleElem.style.left = rectangle.topLeft.x.show
      rectangleElem.style.top = rectangle.topLeft.y.show
      rectangleElem.style.width = rectangle.width.show
      rectangleElem.style.height = rectangle.height.show

  val hideOverlaidRectangle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangle = getOverlaidRectangle
      rectangle.style.visibility = "hidden"
      rectangle.style.borderWidth = 0.px.show
      rectangle.style.width = 0.px.show
      rectangle.style.height = 0.px.show

  private def getOverlaidRectangle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidRectangleId).asInstanceOf[dom.HTMLElement]

end RectangleCommand
