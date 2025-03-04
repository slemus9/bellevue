package bellevue.logic

import bellevue.domain.*
import bellevue.domain.tools.*
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import monocle.syntax.all.*
import tyrian.Cmd

object ToolboxAction extends Variation[DrawingModel, Cmd[IO, Msg]]:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage.isInstanceOf[ToolboxMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialSetter: model =>
    model.receivedMessage match
      case ToolboxMsg.PickColor(color) =>
        model.focus(_.lineConfig.color).replace(color)

      case ToolboxMsg.PickBrushSize(size) =>
        model.focus(_.lineConfig.lineWidth).replace(size)

      case ToolboxMsg.PickTool(tool) if tool == Tool.Eraser =>
        model.copy(selectedTool = Tool.Brush).focus(_.lineConfig.color).replace(Color.White)

      case ToolboxMsg.PickTool(tool) =>
        model.copy(selectedTool = tool)
