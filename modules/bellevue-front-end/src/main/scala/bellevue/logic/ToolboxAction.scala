package bellevue.logic

import bellevue.domain.*
import monocle.syntax.all.*

object ToolboxAction:

  def pickColor(model: DrawingModel, color: String): DrawingModel =
    model.focus(_.brushConfig.color).replace(color)

  def pickBrushSize(model: DrawingModel, size: Pixels): DrawingModel =
    model.focus(_.brushConfig.lineWidth).replace(size)

  def pickTool(model: DrawingModel, tool: Tool): DrawingModel = tool match
    case Tool.Brush     =>
      model.copy(selectedTool = Tool.Brush)
    case Tool.Eraser    =>
      model.copy(selectedTool = Tool.Brush).focus(_.brushConfig.color).replace(BrushConfig.EraserColor)
    case Tool.Rectangle =>
      model.copy(selectedTool = Tool.Rectangle)
