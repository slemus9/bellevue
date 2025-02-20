package bellevue.logic

import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object BrushAction extends DrawingContext:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) =
    (msg, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        (
          model.clickMouse(from),
          canvas.run(_.setLineStyle(model.lineConfig))
        )

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        (
          model.moveMouse(to),
          canvas.run(_.drawLineSegment(from = dragging.latestPosition, to))
        )

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        (
          model.releaseMouse,
          canvas.run(_.drawLineSegment(from = dragging.latestPosition, to))
        )

      case _ => (model, Cmd.None)
