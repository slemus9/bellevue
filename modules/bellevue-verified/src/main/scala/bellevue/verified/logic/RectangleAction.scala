package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.dom.Cmd
import bellevue.verified.dom.Cmd.syntax.*
import bellevue.verified.domain.*
import stainless.lang.*
import stainless.lang.StaticChecks.*

final class RectangleAction extends Variation[DrawingModel, Cmd]:

  override def isActive(model: DrawingModel): Boolean =
    model.selectedTool == Tool.Rectangle && Msg.isMouseMsg(model.receivedMessage)

  override def run(previous: Cmd, model: DrawingModel): (DrawingModel, Cmd) =
    require(this.isActive(model))
    (model.receivedMessage, model.mouseDragging) match
      case (Msg.Mouse(MouseMsg.MouseDown(from)), None()) =>
        (model, previous |+| overlaidRectangle.show)

      case (Msg.Mouse(MouseMsg.MouseMove(to)), Some(dragging)) =>
        val rectangle = Rectangle.FromEdges(from = dragging.startPosition, to)
        (model, previous |+| overlaidRectangle.draw(rectangle))

      case (Msg.Mouse(MouseMsg.MouseUp(to)), Some(dragging)) =>
        val shape = Rectangle.FromEdges(from = dragging.startPosition, to)
        val draw  = previous |+| rectangle.draw(shape) |+| overlaidRectangle.hide
        (model, draw)

      case _ => (model, previous)
