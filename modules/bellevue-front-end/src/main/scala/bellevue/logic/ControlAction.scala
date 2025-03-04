package bellevue.logic

import bellevue.domain.*
import bellevue.html.BellevueHtml
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

object ControlAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage.isInstanceOf[ControlMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    model.receivedMessage match
      case ControlMsg.ResizeCanvas | ControlMsg.HtmlElementLoaded(BellevueHtml.CanvasId) =>
        canvas.run(_.resize)

      case ControlMsg.MapToCanvas(point, toMouseMsg) =>
        canvas.command.map: canvas =>
          toMouseMsg(canvas.relativePositionOf(point))
