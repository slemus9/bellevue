package bellevue.commands

import bellevue.domain.Point
import cats.effect.IO
import tyrian.Cmd

trait RectangleCommand:

  def drawRectangle(topLeft: Point, bottomRight: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      DrawingCanvas.get.context2d.strokeRect(
        x = topLeft.x,
        y = topLeft.y,
        w = bottomRight.x - topLeft.x,
        h = bottomRight.y - topLeft.y
      )
