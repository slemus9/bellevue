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
    mouseDownInterval: Option[MouseDownInterval]
):

  def clickMouse(startPosition: Point) =
    copy(mouseDownInterval = Some(MouseDownInterval.init(startPosition)))

  def moveMouse(to: Point) =
    this.focus(_.mouseDownInterval.some.latestPosition).replace(to)

  def releaseMouse =
    copy(mouseDownInterval = None)

object DrawingModel:

  val init = DrawingModel(
    selectedTool = Tool.Brush,
    lineConfig = LineConfig.init,
    mouseDownInterval = None
  )

/**
  * Models the user interaction of clicking the mouse and moving it around the screen while holding the mouse down
  *
  * @param startPosition
  *   the initial position at which the mouse was clicked
  * @param latestPosition
  *   the latest recorded position of the mouse (without releasing it)
  */
final case class MouseDownInterval(
    startPosition: Point,
    latestPosition: Point
)

object MouseDownInterval:

  def init(startPosition: Point) =
    MouseDownInterval(startPosition, latestPosition = startPosition)
