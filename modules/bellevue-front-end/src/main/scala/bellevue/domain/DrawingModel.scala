package bellevue.domain

final case class DrawingModel(
    isDrawingLine: Boolean,
    linePosition: Point,
    lineColor: String
):
  def updateLinePosition(p: Point) =
    copy(linePosition = p)

  def enableLineDrawing =
    copy(isDrawingLine = true)

  def disableLineDrawing =
    copy(isDrawingLine = false)

  def setLineColor(color: String) =
    copy(lineColor = color)

object DrawingModel:

  val CanvasId         = "drawing-canvas"
  val InitialLineColor = "#0047AB"

  val init = DrawingModel(
    isDrawingLine = false,
    linePosition = Point(0, 0),
    lineColor = InitialLineColor
  )
