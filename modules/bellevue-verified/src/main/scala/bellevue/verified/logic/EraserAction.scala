package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.dom.Cmd
import bellevue.verified.dom.Cmd.syntax.*
import bellevue.verified.domain.*
import stainless.lang.*
import stainless.lang.StaticChecks.*

final class EraserCanvasAction extends Variation[DrawingModel, Cmd]:

  override def isActive(model: DrawingModel): Boolean =
    model.selectedTool == Tool.Eraser &&
      model.mouseDragging.isDefined &&
      (Msg.isMouseMove(model.receivedMessage) || Msg.isMouseUp(model.receivedMessage))

  override def run(previous: Cmd, model: DrawingModel): (DrawingModel, Cmd) =
    require(this.isActive(model))
    model.receivedMessage match
      case Msg.Mouse(MouseMsg.MouseMove(center)) =>
        val to    = Point(center.x, center.y + model.eraserConfig.radius.value)
        val shape = Circle.FromCenter(center, to)
        (model, previous |+| circle.draw(shape))

      case Msg.Mouse(MouseMsg.MouseUp(center)) =>
        val to    = Point(center.x, center.y + model.eraserConfig.radius.value)
        val shape = Circle.FromCenter(center, to)
        (model, previous |+| circle.draw(shape))

final class OverlaidEraserAction extends Variation[DrawingModel, Cmd]:

  override def isActive(model: DrawingModel): Boolean =
    model.selectedTool == Tool.Eraser &&
      (Msg.isMouseMove(model.receivedMessage) || Msg.isMouseUp(model.receivedMessage))

  override def run(previous: Cmd, model: DrawingModel): (DrawingModel, Cmd) =
    require(this.isActive(model))
    model.receivedMessage match
      case Msg.Mouse(MouseMsg.MouseMove(center)) =>
        val to    = Point(center.x, center.y + model.eraserConfig.radius.value)
        val shape = Circle.FromCenter(center, to)
        (model, previous |+| overlaidCircle.draw(shape))

      case Msg.Mouse(MouseMsg.MouseUp(center)) =>
        val to    = Point(center.x, center.y + model.eraserConfig.radius.value)
        val shape = Circle.FromCenter(center, to)
        (model, previous |+| overlaidCircle.draw(shape))
