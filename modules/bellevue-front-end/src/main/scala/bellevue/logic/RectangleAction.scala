package bellevue.logic

import bellevue.domain.*
import bellevue.domain.geometry.Rectangle
import cats.effect.IO
import tyrian.Cmd

object RectangleAction extends DrawingContext:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          canvas.run(_.setLineStyle(model.lineConfig)) |+| overlaidRectangle.run(_.show)
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        (
          model.moveMouse(to),
          overlaidRectangle.run(_.draw(rectangle))
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        (
          model.releaseMouse,
          canvas.run(_.drawRectangle(rectangle)) |+| overlaidRectangle.run(_.hide)
        )

      case _ => (model, Cmd.None)
