package bellevue.domain

final case class DrawingModel(
    selectedTool: Tool,
    isDrawing: Boolean,
    linePosition: Point,
    rectangleStart: Point,
    brushConfig: BrushConfig
)

object DrawingModel:

  val init = DrawingModel(
    selectedTool = Tool.Brush,
    isDrawing = false,
    linePosition = Point(0, 0),
    rectangleStart = Point(0, 0),
    brushConfig = BrushConfig.init
  )

enum Tool:
  case Brush, Eraser, Rectangle
