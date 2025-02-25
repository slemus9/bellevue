package bellevue.logic.context

import bellevue.domain.*
import bellevue.domain.geometry.Rectangle
import bellevue.domain.tools.Tool
import bellevue.logic.context.Variation.Behavior
import cats.effect.IO
import tyrian.Cmd

object RectangleAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.Rectangle

  override def run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    (model.receivedMessage, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        canvas.run(_.setLineStyle(model.lineConfig)) |+| overlaidRectangle.run(_.show)

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        overlaidRectangle.run(_.draw(rectangle))

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        canvas.run(_.drawRectangle(rectangle)) |+| overlaidRectangle.run(_.hide)
