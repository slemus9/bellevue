package bellevue.domain

import bellevue.domain.geometry.{Pixels, Point}
import bellevue.domain.tools.{Color, Tool}

type Msg = ControlMsg | MouseMsg | ToolboxMsg

enum ControlMsg:
  case HtmlElementLoaded(id: String)
  case MapToCanvas(point: Point, buildMsg: Point => MouseMsg)
  case NoAction
  case Partial(parsedMsg: Either[String, Msg])
  case ResizeCanvas

enum MouseMsg:
  case MouseDown(point: Point)
  case MouseMove(point: Point)
  case MouseUp(point: Point)

enum ToolboxMsg:
  case PickBrushSize(size: Pixels)
  case PickColor(color: Color)
  case PickTool(tool: Tool)
