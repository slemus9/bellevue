package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

import scala.math

object CircleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) = msg match
    case MouseMsg.MouseDown(center) =>
      (
        model.copy(isDrawing = true, latestMousePosition = center),
        Command.setLineStyle(model.brushConfig)
      )

    case MouseMsg.MouseMove(_) => (model, Cmd.None)

    case MouseMsg.MouseUp(to) =>
      val center = model.latestMousePosition
      val deltax = center.x - to.x
      val deltay = center.y - to.y
      val radius = math.sqrt(deltax * deltax + deltay * deltay)
      (
        model.copy(isDrawing = false),
        Command.drawCircle(center, radius)
      )
