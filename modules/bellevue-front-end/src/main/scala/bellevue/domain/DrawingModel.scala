package bellevue.domain

final case class DrawingModel(
    selectedTool: Tool,
    isDrawing: Boolean,
    latestMousePosition: Point,
    brushConfig: BrushConfig,
    mouseDownInterval: Option[MouseDownInterval]
)

object DrawingModel:

  val init = DrawingModel(
    selectedTool = Tool.Brush,
    isDrawing = false,
    latestMousePosition = Point(0, 0),
    brushConfig = BrushConfig.init,
    mouseDownInterval = None
  )

enum Tool:
  case Brush, Circle, Eraser, Rectangle

final case class MouseDownInterval(
    startPosition: Point,
    latestPosition: Point
)

object MouseDownInterval:

  def init(startPosition: Point) =
    MouseDownInterval(startPosition, latestPosition = startPosition)
