package bellevue.domain

final case class DrawingModel(
    isDrawingLine: Boolean,
    linePosition: Point,
    brushConfig: BrushConfig
)

object DrawingModel:

  val init = DrawingModel(
    isDrawingLine = false,
    linePosition = Point(0, 0),
    brushConfig = BrushConfig.init
  )
