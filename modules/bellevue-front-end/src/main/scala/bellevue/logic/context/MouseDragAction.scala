package bellevue.logic.context

import bellevue.domain.*
import bellevue.domain.DrawingModel
import bellevue.logic.context.Variation.Behavior
import cats.effect.IO
import tyrian.Cmd

object MouseDragAction extends Variation[DrawingModel, Cmd[IO, Msg]], Variation.AlwaysActive[DrawingModel]:

  override def run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialSetter: model =>
    model.receivedMessage match
      case MouseMsg.MouseDown(from) => model.clickMouse(from)
      case MouseMsg.MouseMove(to)   => model.moveMouse(to)
      case MouseMsg.MouseUp(_)      => model.releaseMouse
