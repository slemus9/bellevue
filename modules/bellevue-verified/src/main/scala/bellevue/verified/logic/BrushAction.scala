package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.dom.Cmd
import bellevue.verified.dom.Cmd.syntax.*
import bellevue.verified.domain.*
import stainless.lang.*
import stainless.lang.StaticChecks.*

final class BrushAction extends Variation[DrawingModel, Cmd]:

  override def isActive(model: DrawingModel): Boolean =
    model.selectedTool == Tool.Brush &&
      model.receivedMessage.isInstanceOf[Msg.Mouse] &&
      model.mouseDragging.isDefined

  override def run(previous: Cmd, model: DrawingModel): (DrawingModel, Cmd) =
    require(this.isActive(model))
    (model.receivedMessage, model.mouseDragging) match
      case (Msg.Mouse(MouseMsg.MouseMove(to)), Some(dragging)) =>
        val draw = previous |+| canvas.drawLineSegment(from = dragging.latestPosition, to)
        (model, draw)

      case (Msg.Mouse(MouseMsg.MouseUp(to)), Some(dragging)) =>
        val draw = previous |+| canvas.drawLineSegment(from = dragging.latestPosition, to)
        (model, draw)

      case _ => (model, previous)
