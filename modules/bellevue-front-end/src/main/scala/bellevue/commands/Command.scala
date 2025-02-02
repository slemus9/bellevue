package bellevue.commands

import bellevue.domain.Point
import cats.effect.IO
import tyrian.Cmd

object Command:

  def initLineDrawing(color: String, lineWidth: Double): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      DrawingCanvas.get.context2d.lineCap = "round"
      DrawingCanvas.get.context2d.strokeStyle = "green"
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
