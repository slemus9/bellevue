package bellevue.logic

import bellevue.domain.*
import bellevue.domain.geometry.Pixels
import bellevue.domain.tools.{Color, Tool}
import monocle.syntax.all.*

object ToolboxAction:

  def pickColor(model: DrawingModel, color: Color): DrawingModel =
    model.focus(_.lineConfig.color).replace(color)

  def pickBrushSize(model: DrawingModel, size: Pixels): DrawingModel =
    model.focus(_.lineConfig.lineWidth).replace(size)

  def pickTool(model: DrawingModel, tool: Tool): DrawingModel = tool match
    case Tool.Eraser =>
      model.copy(selectedTool = Tool.Brush).focus(_.lineConfig.color).replace(Color.White)
    case tool        =>
      model.copy(selectedTool = tool)
