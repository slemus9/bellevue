package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object BrushAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDownInterval) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          Command.setLineStyle(model.lineConfig)
        )

      case (MouseMsg.MouseMove(to), Some(interval)) =>
        (
          model.moveMouse(to),
          Command.drawLineSegment(from = interval.latestPosition, to)
        )

      case (MouseMsg.MouseUp(to), Some(interval)) =>
        (
          model.releaseMouse,
          Command.drawLineSegment(from = interval.latestPosition, to)
        )

      case _ => (model, Cmd.None)
