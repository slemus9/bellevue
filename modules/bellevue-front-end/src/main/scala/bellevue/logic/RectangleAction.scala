package bellevue.logic

import bellevue.commands.*
import bellevue.domain.*
import bellevue.domain.geometry.Rectangle
import cats.effect.IO
import tyrian.Cmd

object RectangleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          Get.canvas.run(_.setLineStyle(model.lineConfig)) |+| Get.overlaidRectangle.run(_.show)
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        (
          model.moveMouse(to),
          Get.overlaidRectangle.run(_.draw(rectangle))
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        (
          model.releaseMouse,
          Get.canvas.run(_.drawRectangle(rectangle)) |+| Get.overlaidRectangle.run(_.hide)
        )

      case _ => (model, Cmd.None)
