package bellevue.logic

import bellevue.domain.{DrawingModel, Msg}
import bellevue.logic.context.Variation
import cats.effect.IO
import tyrian.Cmd

private val DrawAction: Variation[DrawingModel, Cmd[IO, Msg]] =
  Variation.sequence(
    Variation.oneOf(
      BrushAction,
      CircleAction,
      EraserAction,
      RectangleAction
    ),
    MouseDragUpdateAction
  )

val BellevueAction: Variation[DrawingModel, Cmd[IO, Msg]] =
  Variation.oneOf(
    Variation.sequence(ControlAction, ResetStyleAction),
    ToolboxAction,
    DrawAction
  )
