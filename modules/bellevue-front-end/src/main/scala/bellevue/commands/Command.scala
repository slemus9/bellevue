package bellevue.commands

import bellevue.domain.{DrawingModel, Point}
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

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

  def setLineStyle(model: DrawingModel): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      DrawingCanvas.get.context2d.lineCap = "round"
      DrawingCanvas.get.context2d.strokeStyle = model.lineColor
      DrawingCanvas.get.context2d.lineWidth = 2

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
