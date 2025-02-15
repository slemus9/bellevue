package bellevue.commands

import bellevue.domain.Point
import bellevue.html.BellevueHtml
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

import scala.math

trait CircleCommand:

  def drawCircle(center: Point, radius: Double): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val context = DrawingCanvas.get.context2d
      context.beginPath()
      context.arc(
        x = center.x,
        y = center.y,
        radius,
        startAngle = 0,
        endAngle = 2 * math.Pi
      )
      context.stroke()

  val showOverlaidCircle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circle = getOverlaidCircle
      circle.style.visibility = "visible"
      circle.style.borderWidth = "1px"

  def drawOverlaidCircle(center: Point, radius: Double): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circle = getOverlaidCircle
      circle.style.left = s"${center.x - radius}px"
      circle.style.top = s"${center.y - radius}px"
      circle.style.width = s"${2 * radius}px"
      circle.style.height = s"${2 * radius}px"

  val hideOverlaidCircle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circle = getOverlaidCircle
      circle.style.visibility = "hidden"
      circle.style.borderWidth = "0px"
      circle.style.width = "0px"
      circle.style.height = "0px"

  private def getOverlaidCircle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidCircleId).asInstanceOf[dom.HTMLElement]

end CircleCommand
