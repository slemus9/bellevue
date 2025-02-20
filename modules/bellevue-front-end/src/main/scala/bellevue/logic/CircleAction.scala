package bellevue.logic

import bellevue.commands.*
import bellevue.domain.*
import bellevue.domain.geometry.Circle
import cats.effect.IO
import tyrian.Cmd

object CircleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(center), None) =>
        (
          model.clickMouse(center),
          Get.canvas.run(_.setLineStyle(model.lineConfig)) |+| Get.overlaidCircle.run(_.show)
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val circle = Circle(center = dragging.startPosition, to)
        (
          model.moveMouse(to),
          Get.overlaidCircle.run(_.draw(circle))
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val circle = Circle(center = dragging.startPosition, to)
        (
          model.releaseMouse,
          Get.canvas.run(_.drawCircle(circle)) |+| Get.overlaidCircle.run(_.hide)
        )

      case _ => (model, Cmd.None)
