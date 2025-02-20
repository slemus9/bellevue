package bellevue.logic

import bellevue.domain.*
import bellevue.domain.geometry.Circle
import cats.effect.IO
import tyrian.Cmd

object CircleAction extends DrawingContext:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(center), None) =>
        (
          model.clickMouse(center),
          canvas.run(_.setLineStyle(model.lineConfig)) |+| overlaidCircle.run(_.show)
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val circle = Circle(center = dragging.startPosition, to)
        (
          model.moveMouse(to),
          overlaidCircle.run(_.draw(circle))
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val circle = Circle(center = dragging.startPosition, to)
        (
          model.releaseMouse,
          canvas.run(_.drawCircle(circle)) |+| overlaidCircle.run(_.hide)
        )

      case _ => (model, Cmd.None)
