package bellevue.verified.domain

sealed abstract class Tool

object Tool:
  case object Brush     extends Tool
  case object ColorFill extends Tool
  case object Circle    extends Tool
  case object Eraser    extends Tool
  case object Rectangle extends Tool
