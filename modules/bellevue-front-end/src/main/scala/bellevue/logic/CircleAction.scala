package bellevue.logic

import bellevue.dom.DrawingEnvironment
import bellevue.domain.*
import bellevue.domain.geometry.Circle
import bellevue.domain.tools.Tool
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

object CircleAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.Circle && state.receivedMessage.isInstanceOf[MouseMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    (model.receivedMessage, model.mouseDragging) match
      case (MouseMsg.MouseDown(center), None) =>
        sideEffect(_.overlaidCircle.refresh.show)

      case (MouseMsg.MouseMove(to), Some(dragging)) =>
        val circle = Circle(center = dragging.startPosition, to)
        sideEffect(_.overlaidCircle.refresh.draw(circle))

      case (MouseMsg.MouseUp(to), Some(dragging)) =>
        val circle = Circle(center = dragging.startPosition, to)
        sideEffect(_.canvas.refresh.drawCircle(circle)) |+| sideEffect(_.overlaidCircle.refresh.hide)
