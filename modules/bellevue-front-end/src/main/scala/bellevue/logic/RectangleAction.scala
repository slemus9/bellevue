package bellevue.logic

import bellevue.dom.DrawingEnvironment
import bellevue.domain.*
import bellevue.domain.geometry.Rectangle
import bellevue.domain.tools.Tool
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

object RectangleAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.Rectangle && state.receivedMessage.isInstanceOf[MouseMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    (model.receivedMessage, model.mouseDragging) match
      case (MouseMsg.MouseDown(from), None) =>
        sideEffect(_.overlaidRectangle.refresh.show)

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        sideEffect(_.overlaidRectangle.refresh.draw(rectangle))

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val rectangle = Rectangle(from = dragging.startPosition, to)
        sideEffect(_.canvas.refresh.drawRectangle(rectangle)) |+| sideEffect(_.overlaidRectangle.refresh.hide)
