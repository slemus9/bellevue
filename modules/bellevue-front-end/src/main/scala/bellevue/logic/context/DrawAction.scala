package bellevue.logic.context

import bellevue.domain.{DrawingModel, Msg}
import cats.data.State
import cats.effect.IO
import tyrian.Cmd

object DrawAction:

  val stateTransition: State[DrawingModel, Cmd[IO, Msg]] =
    Variation.Monoidal.merge(MouseDragAction, BrushAction, RectangleAction, CircleAction)
