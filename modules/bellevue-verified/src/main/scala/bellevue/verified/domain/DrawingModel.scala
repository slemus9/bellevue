package bellevue.verified.domain

import stainless.lang.Option

final case class DrawingModel(
    selectedTool: Tool,
    brushConfig: StyleConfig,
    colorFillConfig: ColorFillConfig,
    eraserConfig: EraserConfig,
    mouseDragging: Option[MouseDragging],
    receivedMessage: Msg
)

final case class MouseDragging(
    startPosition: Point,
    latestPosition: Point
)

object MouseDragging:

  def init(startPosition: Point) =
    MouseDragging(startPosition, latestPosition = startPosition)
