package bellevue.domain

import bellevue.domain.geometry.Point
import bellevue.domain.tools.LineConfig
import bellevue.domain.tools.Tool
import monocle.syntax.all.*

/**
  * Models the state of the canvas at any given point in time
  */
final case class DrawingModel(
    selectedTool: Tool,
    lineConfig: LineConfig,
    mouseDragging: Option[MouseDragging]
):

  def clickMouse(startPosition: Point) =
    copy(mouseDragging = Some(MouseDragging.init(startPosition)))

  def moveMouse(to: Point) =
    this.focus(_.mouseDragging.some.latestPosition).replace(to)

  def releaseMouse =
    copy(mouseDragging = None)

object DrawingModel:

  val init = DrawingModel(
    selectedTool = Tool.Brush,
    lineConfig = LineConfig.init,
    mouseDragging = None
  )

/**
  * Models the user interaction of clicking the mouse and moving it around the screen while holding the mouse down
  *
  * @param startPosition
  *   the initial position at which the mouse was clicked
  * @param latestPosition
  *   the latest recorded position of the mouse (without releasing it)
  */
final case class MouseDragging(
    startPosition: Point,
    latestPosition: Point
)

object MouseDragging:

  def init(startPosition: Point) =
    MouseDragging(startPosition, latestPosition = startPosition)
