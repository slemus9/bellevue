package bellevue.verified.domain

import stainless.lang.*

final case class DrawingModel(
    selectedTool: Tool,
    brushConfig: StyleConfig,
    colorFillConfig: ColorFillConfig,
    eraserConfig: EraserConfig,
    mouseDragging: Option[MouseDragging],
    receivedMessage: Msg
):

  def clickMouse(startPosition: Point) =
    copy(mouseDragging = Some(MouseDragging.init(startPosition)))

  def moveMouse(to: Point) =
    copy(mouseDragging = mouseDragging.map(_.copy(latestPosition = to)))

  def releaseMouse =
    copy(mouseDragging = None())

final case class MouseDragging(
    startPosition: Point,
    latestPosition: Point
)

object MouseDragging:

  def init(startPosition: Point) =
    MouseDragging(startPosition, latestPosition = startPosition)
