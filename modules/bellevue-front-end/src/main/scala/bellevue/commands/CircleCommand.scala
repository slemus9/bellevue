package bellevue.commands

import bellevue.domain.Point
import cats.effect.IO
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
