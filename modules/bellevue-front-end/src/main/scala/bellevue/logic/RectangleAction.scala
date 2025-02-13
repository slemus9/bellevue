package bellevue.logic

import bellevue.commands.Command
import bellevue.domain.*
import cats.effect.IO
import monocle.syntax.all.*
import tyrian.Cmd

object RectangleAction:

  def draw(model: DrawingModel, msg: MouseMsg): (DrawingModel, Cmd[IO, Msg]) = (msg, model.mouseDownInterval) match
    case (MouseMsg.MouseDown(from), None) =>
      val nextModel = model.copy(
        isDrawing = true,
        latestMousePosition = from,
        mouseDownInterval = Some(MouseDownInterval.init(from))
      )

      val cmd = Command.setLineStyle(model.brushConfig) |+| Command.placeOverlaidRectange(from)

      (nextModel, cmd)

    case (MouseMsg.MouseMove(to), Some(interval)) =>
      val nextModel = model.focus(_.mouseDownInterval.some.latestPosition).replace(to)

      val cmd = Command.growOverlaidRectangle.tupled(
        rectangleCorners(from = interval.startPosition, to)
      )

      (nextModel, cmd)

    case (MouseMsg.MouseUp(to), Some(interval)) =>
      val cmd = Command.drawRectangle.tupled(
        rectangleCorners(from = interval.startPosition, to)
      ) |+| Command.hideOverlaidRectangle

      (model.copy(isDrawing = false, mouseDownInterval = None), cmd)

    case _ => (model, Cmd.None)

  private def rectangleCorners(from: Point, to: Point): (Point, Point) =
    if to.y < from.y then (from, to) else (to, from)

end RectangleAction
