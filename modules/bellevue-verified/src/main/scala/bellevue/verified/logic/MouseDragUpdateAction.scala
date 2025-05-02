package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.dom.Cmd
import bellevue.verified.domain.*
import stainless.lang.*
import stainless.lang.StaticChecks.*

final class MouseDragUpdateAction extends Variation[DrawingModel, Cmd]:

  override def isActive(model: DrawingModel): Boolean =
    Msg.isMouseMsg(model.receivedMessage)

  override def run(previous: Cmd, model: DrawingModel): (DrawingModel, Cmd) =
    require(this.isActive(model))
    model.receivedMessage match
      case Msg.Mouse(MouseMsg.MouseDown(from)) => (model.clickMouse(from), previous)
      case Msg.Mouse(MouseMsg.MouseMove(to))   => (model.moveMouse(to), previous)
      case Msg.Mouse(MouseMsg.MouseUp(_))      => (model.releaseMouse, previous)
