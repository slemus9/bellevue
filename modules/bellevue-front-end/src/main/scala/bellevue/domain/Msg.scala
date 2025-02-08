package bellevue.domain

enum Msg:
  case DrawLineStart(from: Point)
  case DrawLineTo(to: Point)
  case DrawLineEnd
  case PickColor(color: String)
  case PickBrushSize(size: Pixels)
  case ResizeCanvas
  case LoadedElement(elementId: String)
  case Partial(parsed: Either[String, Msg])
  case NoAction
