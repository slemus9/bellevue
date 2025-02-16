package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object BrushAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          Command.setLineStyle(model.lineConfig)
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        (
          model.moveMouse(to),
          Command.drawLineSegment(from = dragging.latestPosition, to)
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        (
          model.releaseMouse,
          Command.drawLineSegment(from = dragging.latestPosition, to)
        )

      case _ => (model, Cmd.None)
