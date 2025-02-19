package bellevue.logic

import bellevue.commands.*
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object BrushAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          Get.canvas.run(_.setLineStyle(model.lineConfig))
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        (
          model.moveMouse(to),
          Get.canvas.run(_.drawLineSegment(from = dragging.latestPosition, to))
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        (
          model.releaseMouse,
          Get.canvas.run(_.drawLineSegment(from = dragging.latestPosition, to))
        )

      case _ => (model, Cmd.None)
