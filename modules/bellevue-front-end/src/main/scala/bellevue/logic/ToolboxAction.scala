package bellevue.logic

import bellevue.domain.*
import bellevue.domain.tools.*
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import monocle.syntax.all.*
import tyrian.Cmd

val ToolboxAction: Variation[DrawingModel, Cmd[IO, Msg]] =
  Variation.sequence(
    EraserActivationAction,
    BaseToolboxAction,
    ApplyStyleAction
  )

object BaseToolboxAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]]:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage.isInstanceOf[ToolboxMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialSetter: model =>
    model.receivedMessage match
      case ToolboxMsg.PickColor(color) =>
        model.focus(_.brushConfig.color).replace(color)

      case ToolboxMsg.PickBrushSize(size) =>
        model.focus(_.brushConfig.lineWidth).replace(size)

      case ToolboxMsg.PickEraserRadius(radius) =>
        model.focus(_.eraserConfig.radius).replace(radius)

      case ToolboxMsg.PickTool(tool) =>
        model.copy(selectedTool = tool)

object EraserActivationAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage.isInstanceOf[ToolboxMsg.PickTool]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    model.receivedMessage match
      case ToolboxMsg.PickTool(Tool.Eraser) =>
        overlaidCircle.run(_.show)

      case ToolboxMsg.PickTool(_) if model.selectedTool == Tool.Eraser =>
        overlaidCircle.run(_.hide)

object ApplyStyleAction extends SetStyleAction, DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage.isInstanceOf[ToolboxMsg]
