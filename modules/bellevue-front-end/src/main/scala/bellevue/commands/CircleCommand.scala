package bellevue.commands

import bellevue.domain.geometry.Circle
import bellevue.html.BellevueHtml
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

import scala.math

trait CircleCommand:

  def drawCircle(circle: Circle): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val context = DrawingCanvas.get.context2d
      context.beginPath()
      context.arc(
        x = circle.center.x,
        y = circle.center.y,
        circle.radius,
        startAngle = 0,
        endAngle = 2 * math.Pi
      )
      context.stroke()

  val showOverlaidCircle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circle = getOverlaidCircle
      circle.style.visibility = "visible"
      circle.style.borderWidth = "1px"

  def drawOverlaidCircle(circle: Circle): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circleDiv = getOverlaidCircle
      circleDiv.style.left = s"${circle.center.x - circle.radius}px"
      circleDiv.style.top = s"${circle.center.y - circle.radius}px"
      circleDiv.style.width = s"${circle.diameter}px"
      circleDiv.style.height = s"${circle.diameter}px"

  val hideOverlaidCircle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circleDiv = getOverlaidCircle
      circleDiv.style.visibility = "hidden"
      circleDiv.style.borderWidth = "0px"
      circleDiv.style.width = "0px"
      circleDiv.style.height = "0px"

  private def getOverlaidCircle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidCircleId).asInstanceOf[dom.HTMLElement]

end CircleCommand
