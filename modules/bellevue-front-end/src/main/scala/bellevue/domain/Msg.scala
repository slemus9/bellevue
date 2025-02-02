package bellevue.domain

enum Msg:
  case DrawLineStart(from: Point)
  case DrawLineTo(to: Point)
  case DrawLineEnd
  case Noop
