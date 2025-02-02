package bellevue.domain

final case class DrawingModel(
    isDrawingLine: Boolean,
    linePosition: Point
):
  def updateLinePosition(p: Point) =
    copy(linePosition = p)

  def enableLineDrawing =
    copy(isDrawingLine = true)

  def disableLineDrawing =
    copy(isDrawingLine = false)

object DrawingModel:

  val init = DrawingModel(
    isDrawingLine = false,
    linePosition = Point(0, 0)
  )
