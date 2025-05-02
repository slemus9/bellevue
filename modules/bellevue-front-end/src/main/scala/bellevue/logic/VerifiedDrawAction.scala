package bellevue.logic

import bellevue.domain.{DrawingModel, Msg}
import bellevue.logic.context.Behavior
import bellevue.logic.context.Variation
import bellevue.transformers.all.{andThen, orElse, transformCommand, given}
import bellevue.verified.dom.Cmd as VCmd
import bellevue.verified.logic as verified
import cats.data.State
import cats.effect.IO
import io.scalaland.chimney.dsl.transformInto
import tyrian.Cmd

/**
  * TODO: Use a common command language between the verified package and the front-end package, which can then be
  * transformed to tyrian's Cmd, so that we can avoid this ugly workaround
  */
val VerifiedDrawAction: Variation[DrawingModel, Cmd[IO, Msg]] =
  val eraserAction = new verified.EraserCanvasAction().andThen(new verified.OverlaidEraserAction)

  val drawAction: Variation[DrawingModel, VCmd] = new verified.BrushAction()
    .orElse(new verified.CircleAction)
    .orElse(eraserAction)
    .orElse(new verified.RectangleAction)
    .andThen(new verified.MouseDragUpdateAction)
    .transformInto

  new Variation[DrawingModel, Cmd[IO, Msg]]:

    override def isActive(state: DrawingModel): Boolean =
      drawAction.isActive(state)

    override protected def run: Behavior[DrawingModel, Cmd[IO, Msg]] = _ =>
      State { state =>
        val (newState, result) = drawAction.runWith(state, VCmd.None)
        val newResult          = result.transformCommand
        (newState, newResult)
      }
