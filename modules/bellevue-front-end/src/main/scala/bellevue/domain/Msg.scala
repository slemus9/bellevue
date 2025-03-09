package bellevue.domain

import bellevue.domain.geometry.{Pixels, Point, RGB}
import bellevue.domain.tools.Tool

type Msg = ControlMsg | MouseMsg | Msg.Partial | ToolboxMsg

object Msg:

  final case class Partial(msgOrError: Either[AppError, Msg])

enum ControlMsg:
  case HtmlElementLoaded(id: String)
  case MapToCanvas(point: Point, toMouseMsg: Point => MouseMsg)
  case NoAction
  case ResizeCanvas

enum MouseMsg:
  case MouseDown(point: Point)
  case MouseMove(point: Point)
  case MouseUp(point: Point)

enum ToolboxMsg:
  case PickBrushSize(size: Pixels)
  case PickColor(color: RGB)
  case PickEraserRadius(radius: Pixels)
  case PickTool(tool: Tool)
