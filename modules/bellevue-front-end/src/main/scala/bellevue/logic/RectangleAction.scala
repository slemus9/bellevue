package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import bellevue.domain.geometry.Rectangle
import cats.effect.IO
import tyrian.Cmd

object RectangleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDownInterval) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          Command.setLineStyle(model.lineConfig) |+| Command.showOverlaidRectange
        )

      case (MouseMsg.MouseMove(to), Some(interval)) =>
        val rectangle = Rectangle(from = interval.startPosition, to)
        (
          model.moveMouse(to),
          Command.drawOverlaidRectangle(rectangle)
        )

      case (MouseMsg.MouseUp(to), Some(interval)) =>
        val rectangle = Rectangle(from = interval.startPosition, to)
        (
          model.releaseMouse,
          Command.drawRectangle(rectangle) |+| Command.hideOverlaidRectangle
        )

      case _ => (model, Cmd.None)
