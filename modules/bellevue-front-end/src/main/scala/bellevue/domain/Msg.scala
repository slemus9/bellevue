package bellevue.domain

type Msg = BrushMsg | ControlMsg | EraserMsg | ToolboxMsg

enum ControlMsg:
  case Partial(parsedMsg: Either[String, Msg])
  case HtmlElementLoaded(id: String)
  case ResizeCanvas
  case NoAction

enum EraserMsg:
  case Enable

enum BrushMsg:
  case Start(point: Point)
  case To(point: Point)
  case End

enum ToolboxMsg:
  case PickColor(color: String)
  case PickBrushSize(size: Pixels)
