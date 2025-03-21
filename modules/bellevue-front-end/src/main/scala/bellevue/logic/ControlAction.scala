package bellevue.logic

import bellevue.dom.DrawingEnvironment
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
        sideEffect(_.canvas.refresh.resize)

      case ControlMsg.MapToCanvas(point, toMouseMsg) =>
        command { elems =>
          if elems.toolboxElement.refresh.contains(point)
          then ControlMsg.NoAction
          else toMouseMsg(elems.canvas.refresh.relativePositionOf(point))
        }

object ResetStyleAction extends SetStyleAction, DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage match
      case ControlMsg.ResizeCanvas | ControlMsg.HtmlElementLoaded(BellevueHtml.CanvasId) => true
      case _                                                                             => false
