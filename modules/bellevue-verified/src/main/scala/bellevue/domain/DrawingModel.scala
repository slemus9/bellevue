package bellevue.domain

import stainless.lang.Option

final case class DrawingModel(
    selectedTool: Tool,
    mouseDragging: Option[MouseDragging],
    receivedMessage: Msg,
    canvasImage: Option[Image]
)

final case class MouseDragging(
    startPosition: Point,
    latestPosition: Point
)

object MouseDragging:

  def init(startPosition: Point) =
    MouseDragging(startPosition, latestPosition = startPosition)
