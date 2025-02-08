package bellevue.domain

enum Msg:
  case DrawLineStart(from: Point)
  case DrawLineTo(to: Point)
  case DrawLineEnd
  case PickColor(color: String)
  case ResizeCanvas
  case LoadedElement(elementId: String)
  case NoAction
