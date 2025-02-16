package bellevue.commands

import bellevue.domain.geometry.Point
import bellevue.domain.tools.LineConfig
import cats.effect.IO
import tyrian.Cmd

trait BrushCommand:

  def setLineStyle(config: LineConfig): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val context = DrawingCanvas.get.context2d
      context.lineCap = "round"
      context.strokeStyle = config.color
      context.lineWidth = config.lineWidth

  def drawLineSegment(from: Point, to: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val context = DrawingCanvas.get.context2d
      context.beginPath()
      context.moveTo(from.x, from.y)
      context.lineTo(to.x, to.y)
      context.stroke()
