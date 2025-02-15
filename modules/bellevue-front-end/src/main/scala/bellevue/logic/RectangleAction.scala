package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object RectangleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDownInterval) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          Command.setLineStyle(model.brushConfig) |+| Command.showOverlaidRectange
        )

      case (MouseMsg.MouseMove(to), Some(interval)) =>
        (
          model.moveMouse(to),
          Command.drawOverlaidRectangle(from = interval.startPosition, to)
        )

      case (MouseMsg.MouseUp(to), Some(interval)) =>
        (
          model.releaseMouse,
          Command.drawRectangle(from = interval.startPosition, to) |+| Command.hideOverlaidRectangle
        )

      case _ => (model, Cmd.None)
