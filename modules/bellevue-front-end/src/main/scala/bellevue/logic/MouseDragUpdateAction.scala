package bellevue.logic

import bellevue.domain.*
import bellevue.domain.DrawingModel
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

object MouseDragUpdateAction extends Variation[DrawingModel, Cmd[IO, Msg]]:

  override def isActive(state: DrawingModel): Boolean =
    state.receivedMessage.isInstanceOf[MouseMsg]

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialSetter: model =>
    model.receivedMessage match
      case MouseMsg.MouseDown(from) => model.clickMouse(from)
      case MouseMsg.MouseMove(to)   => model.moveMouse(to)
      case MouseMsg.MouseUp(_)      => model.releaseMouse
