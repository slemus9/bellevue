package bellevue.logic

import bellevue.commands.*
import bellevue.domain.*
import cats.effect.IO
import tyrian.Cmd

object ControlAction:

  val resizeCanvas: Cmd[IO, Nothing] =
    Get.canvas.run(_.resize)

  def mapToCanvasPosition(msg: ControlMsg.MapToCanvas): Cmd[IO, Msg] =
    Get.canvas.command.map: canvas =>
      msg.toMouseMsg(canvas.relativePositionOf(msg.point))
