package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.dom.Cmd
import bellevue.verified.dom.Cmd.syntax.*
import bellevue.verified.domain.*
import stainless.lang.*
import stainless.lang.StaticChecks.*

final class CircleAction extends Variation[DrawingModel, Cmd]:

  override def isActive(model: DrawingModel): Boolean =
    model.selectedTool == Tool.Circle && model.receivedMessage.isInstanceOf[Msg.Mouse]

  override def run(previous: Cmd, model: DrawingModel): (DrawingModel, Cmd) =
    require(this.isActive(model))
    (model.receivedMessage, model.mouseDragging) match
      case (Msg.Mouse(MouseMsg.MouseDown(from)), None()) =>
        (model, previous |+| overlaidCircle.show)

      case (Msg.Mouse(MouseMsg.MouseMove(to)), Some(dragging)) =>
        val circle = Circle.FromCenter(center = dragging.startPosition, to)
        (model, previous |+| overlaidCircle.draw(circle))

      case (Msg.Mouse(MouseMsg.MouseUp(to)), Some(dragging)) =>
        val shape = Circle.FromCenter(center = dragging.startPosition, to)
        val draw  = previous |+| circle.draw(shape) |+| overlaidCircle.hide
        (model, draw)

      case _ => (model, previous)
