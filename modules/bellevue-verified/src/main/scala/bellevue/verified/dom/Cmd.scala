package bellevue.verified.dom

import bellevue.verified.domain.Point

sealed abstract class Cmd

object Cmd:

  final case class DrawLineSegment(from: Point, to: Point)

  sealed abstract class OverlaidShapeCmd
  object OverlaidShapeCmd:
    case object ShowCmd extends OverlaidShapeCmd
    case object HideCmd extends OverlaidShapeCmd

  final case class OverlaidCircleCmd(shapeCmd: OverlaidShapeCmd) extends Cmd

  final case class OverlaidRectangleCmd(shapeCmd: OverlaidRectangleCmd) extends Cmd
