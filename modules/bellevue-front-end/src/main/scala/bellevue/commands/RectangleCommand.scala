package bellevue.commands

import bellevue.domain.Point
import bellevue.html.BellevueHtml
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

trait RectangleCommand:

  def drawRectangle(from: Point, to: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val (topLeft, bottomRight) = rectangleCorners(from, to)
      DrawingCanvas.get.context2d.strokeRect(
        x = topLeft.x,
        y = topLeft.y,
        w = bottomRight.x - topLeft.x,
        h = bottomRight.y - topLeft.y
      )

  def placeOverlaidRectange(topLeft: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangle = getOverlaidRectangle
      rectangle.style.visibility = "visible"
      rectangle.style.borderWidth = "1px"

  def growOverlaidRectangle(from: Point, to: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val (topLeft, bottomRight) = rectangleCorners(from, to)
      val rectangle              = getOverlaidRectangle
      rectangle.style.left = s"${topLeft.x}px"
      rectangle.style.top = s"${topLeft.y}px"
      rectangle.style.width = s"${bottomRight.x - topLeft.x}px"
      rectangle.style.height = s"${bottomRight.y - topLeft.y}px"

  val hideOverlaidRectangle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val rectangle = getOverlaidRectangle
      rectangle.style.visibility = "hidden"
      rectangle.style.borderWidth = "0px"
      rectangle.style.width = "0px"
      rectangle.style.height = "0px"

  private def getOverlaidRectangle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidRectangleId).asInstanceOf[dom.HTMLElement]

  /**
    * @param from
    *   an edge of the rectangle
    * @param to
    *   an edge of the rectangle
    * @return
    *   A tuple containing the top left and bottom right edges (respectively) of the rectangle that is formed by
    *   [[from]] and [[to]]
    */
  def rectangleCorners(from: Point, to: Point): (Point, Point) = (from, to) match
    case (Point(x1, y1), Point(x2, y2)) if y1 < y2 && x1 < x2 => (from, to)
    case (Point(x1, y1), Point(x2, y2)) if y1 < y2            => (Point(x2, y1), Point(x1, y2))
    case (Point(x1, y1), Point(x2, y2)) if x1 < x2            => (Point(x1, y2), Point(x2, y1))
    case (Point(x1, y1), Point(x2, y2))                       => (to, from)

end RectangleCommand
