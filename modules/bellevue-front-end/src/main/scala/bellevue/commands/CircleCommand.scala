package bellevue.commands

import bellevue.domain.geometry.Circle
import bellevue.domain.geometry.Pixels.px
import bellevue.html.BellevueHtml
import cats.effect.IO
import cats.syntax.show.*
import org.scalajs.dom
import tyrian.Cmd

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
      circle.style.borderWidth = 1.px.show

  def drawOverlaidCircle(circle: Circle): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circleDiv = getOverlaidCircle
      circleDiv.style.left = (circle.center.x - circle.radius).px.show
      circleDiv.style.top = (circle.center.y - circle.radius).px.show
      circleDiv.style.width = circle.diameter.show
      circleDiv.style.height = circle.diameter.show

  val hideOverlaidCircle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val circleDiv = getOverlaidCircle
      circleDiv.style.visibility = "hidden"
      circleDiv.style.borderWidth = 0.px.show
      circleDiv.style.width = 0.px.show
      circleDiv.style.height = 0.px.show

  private def getOverlaidCircle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidCircleId).asInstanceOf[dom.HTMLElement]

end CircleCommand
