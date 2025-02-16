package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import bellevue.domain.geometry.Circle
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
        val circle = Circle(center = interval.startPosition, to)
        (
          model.moveMouse(to),
          Command.drawOverlaidCircle(circle)
        )

      case (MouseMsg.MouseUp(to), Some(interval)) =>
        val circle = Circle(center = interval.startPosition, to)
        (
          model.releaseMouse,
          Command.drawCircle(circle) |+| Command.hideOverlaidCircle
        )

      case _ => (model, Cmd.None)
