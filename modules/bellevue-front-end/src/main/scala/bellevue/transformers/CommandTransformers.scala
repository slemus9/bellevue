package bellevue.transformers

import bellevue.dom.DrawingEnvironment
import bellevue.domain.Msg
import bellevue.verified.dom as verified
import cats.effect.IO
import io.scalaland.chimney.dsl.transformInto
import io.scalaland.chimney.Transformer
import tyrian.Cmd

trait CommandTransformers:
  self: DrawingEnvironment & GeometryTransformers =>

  extension (cmd: verified.Cmd)
    def transformCommand: Cmd[IO, Msg] = cmd match
      case verified.Cmd.None =>
        Cmd.None

      case verified.Cmd.Combine(cmd1, cmd2) =>
        cmd1.transformCommand.combine(cmd2.transformCommand)

      case verified.Cmd.DrawLineSegment(from, to) =>
        sideEffect(_.canvas.refresh.drawLineSegment(from.transformInto, to.transformInto))

      case verified.Cmd.DrawCircle(circle) =>
        sideEffect(_.canvas.refresh.drawCircle(circle.transformInto))

      case verified.Cmd.DrawRectangle(rectangle) =>
        sideEffect(_.canvas.refresh.drawRectangle(rectangle.transformInto))

      case verified.Cmd.OverlaidCircle.Show =>
        sideEffect(_.overlaidCircle.refresh.show)

      case verified.Cmd.OverlaidCircle.Hide =>
        sideEffect(_.overlaidCircle.refresh.hide)

      case verified.Cmd.OverlaidCircle.Draw(circle) =>
        sideEffect(_.overlaidCircle.refresh.draw(circle.transformInto))

      case verified.Cmd.OverlaidRectangle.Show =>
        sideEffect(_.overlaidRectangle.refresh.show)

      case verified.Cmd.OverlaidRectangle.Hide =>
        sideEffect(_.overlaidRectangle.refresh.hide)

      case verified.Cmd.OverlaidRectangle.Draw(rectangle) =>
        sideEffect(_.overlaidRectangle.refresh.draw(rectangle.transformInto))
