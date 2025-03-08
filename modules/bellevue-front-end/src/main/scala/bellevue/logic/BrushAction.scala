package bellevue.logic

import bellevue.domain.*
import bellevue.domain.tools.Tool
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

object BrushAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.Brush && state.receivedMessage.isInstanceOf[MouseMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    (model.receivedMessage, model.mouseDragging) match
      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        canvas.run(_.drawLineSegment(from = dragging.latestPosition, to))

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        canvas.run(_.drawLineSegment(from = dragging.latestPosition, to))
