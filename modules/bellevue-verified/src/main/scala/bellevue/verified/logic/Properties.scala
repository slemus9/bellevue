package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.context.Variation.Chainable
import bellevue.verified.dom.Cmd
import bellevue.verified.domain.DrawingModel

object Properties:

  val brushChain = new Chainable[DrawingModel, Cmd]:

    override def v1: Variation[DrawingModel, Cmd] = new BrushAction

    override def v2: Variation[DrawingModel, Cmd] = new MouseDragUpdateAction
