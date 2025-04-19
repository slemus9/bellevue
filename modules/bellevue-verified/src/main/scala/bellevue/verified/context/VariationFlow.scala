package bellevue.verified.context

import bellevue.domain.*
import stainless.annotation.law
import stainless.collection.List
import stainless.lang.*
import stainless.lang.BooleanDecorations

object VariationFlow:

  abstract class OneOfChainable[S, A]:

    def alternatives: List[Variation[S, A]]

    def next: Variation[S, A]

    @law
    def isChainable(previous: A, state: S): Boolean =
      alternatives.exists { v =>
        v.isActive(state) ==> {
          val (newState, _) = v.run(previous, state)
          next.isActive(newState)
        }
      }
      // alternatives.nonEmpty && {
      //   val v = alternatives.head
      //   v.isActive(state) ==> {
      //     val (newState, _) = v.run(previous, state)
      //     next.isActive(newState)
      //   }
      // }

  // Example
  final class BrushAction extends Variation[DrawingModel, Msg]:

    override def isActive(state: DrawingModel): Boolean =
      state.selectedTool == Tool.Brush && state.receivedMessage.isInstanceOf[Msg.Mouse]

    override def run(previous: Msg, state: DrawingModel): (DrawingModel, Msg) = (state, previous)

  final class CircleAction extends Variation[DrawingModel, Msg]:

    override def isActive(state: DrawingModel): Boolean =
      state.selectedTool == Tool.Circle && state.receivedMessage.isInstanceOf[Msg.Mouse]

    override def run(previous: Msg, state: DrawingModel): (DrawingModel, Msg) = (state, previous)

  final class ColorFillAction extends Variation[DrawingModel, Msg]:

    override def isActive(state: DrawingModel): Boolean =
      state.selectedTool == Tool.ColorFill &&
        state.receivedMessage.isInstanceOf[Msg.Mouse] &&
        state.canvasImage.isDefined

    override def run(previous: Msg, state: DrawingModel): (DrawingModel, Msg) = (state, previous)

  final class RectangleAction extends Variation[DrawingModel, Msg]:

    override def isActive(state: DrawingModel): Boolean =
      state.selectedTool == Tool.Rectangle && state.receivedMessage.isInstanceOf[Msg.Mouse]

    override def run(previous: Msg, state: DrawingModel): (DrawingModel, Msg) = (state, previous)

  final class MouseDragUpdateAction extends Variation[DrawingModel, Msg]:

    override def isActive(state: DrawingModel): Boolean =
      state.receivedMessage.isInstanceOf[Msg.Mouse]

    override def run(previous: Msg, state: DrawingModel): (DrawingModel, Msg) = (state, previous)

  // val oneOf = new OneOfChainable[DrawingModel, Msg]:

  //   override def alternatives: List[Variation[DrawingModel, Msg]] =
  //     List(new BrushAction, new CircleAction, new ColorFillAction, new RectangleAction)

  //   override def next: Variation[DrawingModel, Msg] = new MouseDragUpdateAction

  def oneOfLaw(previous: Msg, state: DrawingModel): Boolean =
    val drawActions     = List(new BrushAction, new CircleAction, new ColorFillAction, new RectangleAction)
    val mouseDragUpdate = new MouseDragUpdateAction
    drawActions.forall { action =>
      action.isActive(state) ==> {
        val (newState, _) = action.run(previous, state)
        mouseDragUpdate.isActive(newState)
      }
    }.holds

  val chain = new Variation.Chainable[DrawingModel, Msg]:
    override def v1: Variation[DrawingModel, Msg] = new BrushAction
    override def v2: Variation[DrawingModel, Msg] = new MouseDragUpdateAction

end VariationFlow
