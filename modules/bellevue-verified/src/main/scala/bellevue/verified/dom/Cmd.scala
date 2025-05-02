package bellevue.verified.dom

import bellevue.verified.domain.*

sealed abstract class Cmd:

  def |+|(that: Cmd) = Cmd.Combine(this, that)

object Cmd:

  case object None extends Cmd

  final case class Combine(cmd1: Cmd, cmd2: Cmd) extends Cmd

  final case class DrawLineSegment(from: Point, to: Point) extends Cmd

  final case class DrawCircle(circle: Circle.FromCenter) extends Cmd

  final case class DrawRectangle(rectangle: Rectangle.FromEdges) extends Cmd

  sealed abstract class OverlaidCircle extends Cmd
  object OverlaidCircle:
    case object Show                                 extends OverlaidCircle
    case object Hide                                 extends OverlaidCircle
    final case class Draw(circle: Circle.FromCenter) extends OverlaidCircle

  sealed abstract class OverlaidRectangle extends Cmd
  object OverlaidRectangle:
    case object Show                                      extends OverlaidRectangle
    case object Hide                                      extends OverlaidRectangle
    final case class Draw(rectangle: Rectangle.FromEdges) extends OverlaidRectangle

  object syntax:

    object canvas:
      def drawLineSegment(from: Point, to: Point) = DrawLineSegment(from, to)

    object circle:
      def draw(circle: Circle.FromCenter) = DrawCircle(circle)

    object rectangle:
      def draw(rectangle: Rectangle.FromEdges) = DrawRectangle(rectangle)

    object overlaidCircle:
      val show                            = OverlaidCircle.Show
      val hide                            = OverlaidCircle.Hide
      def draw(circle: Circle.FromCenter) = OverlaidCircle.Draw(circle)

    object overlaidRectangle:
      val show                                 = OverlaidRectangle.Show
      val hide                                 = OverlaidRectangle.Hide
      def draw(rectangle: Rectangle.FromEdges) = OverlaidRectangle.Draw(rectangle)
end Cmd
