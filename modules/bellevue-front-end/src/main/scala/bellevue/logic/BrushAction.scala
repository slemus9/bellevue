package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object BrushAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) = msg match
    case MouseMsg.MouseDown(from) =>
      (
        model.copy(isDrawing = true, latestMousePosition = from),
        Command.setLineStyle(model.brushConfig)
      )

    case MouseMsg.MouseMove(to) if model.isDrawing =>
      (
        model.copy(latestMousePosition = to),
        Command.drawLineSegment(from = model.latestMousePosition, to)
      )

    case MouseMsg.MouseUp(to) =>
      (
        model.copy(isDrawing = false),
        Command.drawLineSegment(from = model.latestMousePosition, to)
      )

    case _ => (model, Cmd.None)
