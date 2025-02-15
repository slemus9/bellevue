package bellevue.commands

import bellevue.domain.Point
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

object Command extends BrushCommand, CircleCommand, RectangleCommand:

  val resizeCanvas: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val canvas         = DrawingCanvas.get
      val context        = canvas.context2d
      val parentBox      = canvas.parentNode.asInstanceOf[dom.Element].getBoundingClientRect()
      val currentDrawing = context.getImageData(0, 0, canvas.width - 1, canvas.height - 1)
      canvas.width = parentBox.width.toInt
      canvas.height = parentBox.height.toInt
      context.putImageData(currentDrawing, 0, 0)

  def mapToCanvasPosition(point: Point): Cmd[IO, Point] =
    Cmd.Run:
      IO(DrawingCanvas.get.relativePosition(point))
