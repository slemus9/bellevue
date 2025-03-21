package bellevue.logic

import bellevue.dom.DrawingEnvironment
import bellevue.domain.*
import bellevue.domain.geometry.Circle
import bellevue.domain.tools.Tool
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

val EraserAction: Variation[DrawingModel, Cmd[IO, Msg]] =
  Variation.sequence(OverlaidEraserAction, EraserCanvasAction)

object EraserCanvasAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.Eraser &&
      state.mouseDragging.isDefined &&
      state.receivedMessage.isInstanceOf[MouseMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    model.receivedMessage match
      case MouseMsg.MouseMove(to) =>
        val circle = Circle(center = to, radius = model.eraserConfig.radius)
        sideEffect(_.canvas.refresh.drawCircle(circle))

      case MouseMsg.MouseUp(to) =>
        val circle = Circle(center = to, radius = model.eraserConfig.radius)
        sideEffect(_.canvas.refresh.drawCircle(circle))

object OverlaidEraserAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.Eraser && state.receivedMessage.isInstanceOf[MouseMsg.MouseMove]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    model.receivedMessage match
      case MouseMsg.MouseMove(to) =>
        val circle = Circle(center = to, radius = model.eraserConfig.radius)
        sideEffect(_.overlaidCircle.refresh.draw(circle))
