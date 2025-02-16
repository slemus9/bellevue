package bellevue.commands

import bellevue.domain.geometry.Rectangle
import bellevue.html.BellevueHtml
import cats.effect.IO
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
      rectangle.style.borderWidth = "1px"

  def drawOverlaidRectangle(rectangle: Rectangle): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangleElem = getOverlaidRectangle
      rectangleElem.style.left = s"${rectangle.topLeft.x}px"
      rectangleElem.style.top = s"${rectangle.topLeft.y}px"
      rectangleElem.style.width = s"${rectangle.width}px"
      rectangleElem.style.height = s"${rectangle.height}px"

  val hideOverlaidRectangle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangle = getOverlaidRectangle
      rectangle.style.visibility = "hidden"
      rectangle.style.borderWidth = "0px"
      rectangle.style.width = "0px"
      rectangle.style.height = "0px"

  private def getOverlaidRectangle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidRectangleId).asInstanceOf[dom.HTMLElement]

end RectangleCommand
