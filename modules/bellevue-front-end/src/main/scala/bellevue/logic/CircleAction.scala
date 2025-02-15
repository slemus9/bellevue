package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object CircleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDownInterval) match
      case (MouseMsg.MouseDown(center), None) =>
        (
          model.clickMouse(center),
          Command.setLineStyle(model.brushConfig) |+| Command.showOverlaidCircle
        )

      case (MouseMsg.MouseMove(to), Some(interval)) =>
        val center = interval.startPosition
        (
          model.moveMouse(to),
          Command.drawOverlaidCircle(center, center.distanceTo(to))
        )

      case (MouseMsg.MouseUp(to), Some(interval)) =>
        val center = interval.startPosition
        (
          model.releaseMouse,
          Command.drawCircle(center, center.distanceTo(to)) |+| Command.hideOverlaidCircle
        )

      case _ => (model, Cmd.None)
