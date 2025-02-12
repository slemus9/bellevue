package bellevue.commands

import bellevue.domain.*
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

import scala.math

object Command:

  val resizeCanvas: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val canvas         = DrawingCanvas.get
      val context        = canvas.context2d
      val parentBox      = canvas.parentNode.asInstanceOf[dom.Element].getBoundingClientRect()
      val currentDrawing = context.getImageData(0, 0, canvas.width - 1, canvas.height - 1)
      canvas.width = parentBox.width.toInt
      canvas.height = parentBox.height.toInt
      context.putImageData(currentDrawing, 0, 0)

  def setLineStyle(config: BrushConfig): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val context = DrawingCanvas.get.context2d
      context.lineCap = "round"
      context.strokeStyle = config.color
      context.lineWidth = config.lineWidth

  def drawLineSegment(from: Point, to: Point): Cmd[IO, Nothing] =
    val canvas = DrawingCanvas.get
    drawLineSegment(
      canvas,
      from = canvas.relativePosition(from),
      to = canvas.relativePosition(to)
    )

  private def drawLineSegment(canvas: DrawingCanvas, from: Point, to: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val context = canvas.context2d
      context.beginPath()
      context.moveTo(from.x, from.y)
      context.lineTo(to.x, to.y)
      context.stroke()

  def drawRectangle(topLeft: Point, bottomRight: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      DrawingCanvas.get.context2d.strokeRect(
        x = topLeft.x,
        y = topLeft.y,
        w = bottomRight.x - topLeft.x,
        h = bottomRight.y - topLeft.y
      )

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

end Command
