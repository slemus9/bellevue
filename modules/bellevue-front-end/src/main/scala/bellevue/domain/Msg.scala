package bellevue.domain

type Msg = ControlMsg | MouseMsg | ToolboxMsg

enum ControlMsg:
  case Partial(parsedMsg: Either[String, Msg])
  case HtmlElementLoaded(id: String)
  case ResizeCanvas
  case NoAction

enum MouseMsg:
  case MouseDown(point: Point)
  case MouseMove(point: Point)
  case MouseUp(point: Point)

enum ToolboxMsg:
  case PickBrushSize(size: Pixels)
  case PickColor(color: String)
  case PickTool(tool: Tool)
