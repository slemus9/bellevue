package bellevue.domain

final case class DrawingModel(
    selectedTool: Tool,
    isDrawing: Boolean,
    latestMousePosition: Point,
    brushConfig: BrushConfig
)

object DrawingModel:

  val init = DrawingModel(
    selectedTool = Tool.Brush,
    isDrawing = false,
    latestMousePosition = Point(0, 0),
    brushConfig = BrushConfig.init
  )

enum Tool:
  case Brush, Circle, Eraser, Rectangle
