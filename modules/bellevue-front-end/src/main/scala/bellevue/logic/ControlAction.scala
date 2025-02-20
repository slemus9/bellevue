package bellevue.logic

import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object ControlAction extends DrawingContext:

  val resizeCanvas: Cmd[IO, Nothing] =
    canvas.run(_.resize)

  def mapToCanvasPosition(msg: ControlMsg.MapToCanvas): Cmd[IO, Msg] =
    canvas.command.map: canvas =>
      msg.toMouseMsg(canvas.relativePositionOf(msg.point))
