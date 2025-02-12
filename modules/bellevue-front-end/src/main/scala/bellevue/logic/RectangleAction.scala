package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object RectangleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) = msg match
    case MouseMsg.MouseDown(from) =>
      (
        model.copy(isDrawing = true, latestMousePosition = from),
        Command.setLineStyle(model.brushConfig)
      )

    case MouseMsg.MouseMove(_) if model.isDrawing => (model, Cmd.None)

    case MouseMsg.MouseUp(to) =>
      val from                   = model.latestMousePosition
      val (topLeft, bottomRight) = if to.y < from.y then (from, to) else (to, from)

      (
        model.copy(isDrawing = false),
        Command.drawRectangle(topLeft, bottomRight)
      )

    case _ => (model, Cmd.None)
