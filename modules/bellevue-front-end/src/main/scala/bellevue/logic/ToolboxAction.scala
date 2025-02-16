package bellevue.logic

import bellevue.domain.*
import bellevue.domain.geometry.Pixels
import monocle.syntax.all.*

object ToolboxAction:

  def pickColor(model: DrawingModel, color: String): DrawingModel =
    model.focus(_.brushConfig.color).replace(color)

  def pickBrushSize(model: DrawingModel, size: Pixels): DrawingModel =
    model.focus(_.brushConfig.lineWidth).replace(size)

  def pickTool(model: DrawingModel, tool: Tool): DrawingModel = tool match
    case Tool.Eraser =>
      model.copy(selectedTool = Tool.Brush).focus(_.brushConfig.color).replace(BrushConfig.EraserColor)
    case tool        =>
      model.copy(selectedTool = tool)
