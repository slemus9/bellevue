package bellevue.verified.logic

import bellevue.verified.context.Variation
import bellevue.verified.context.Variation.Chainable
import bellevue.verified.dom.Cmd
import bellevue.verified.domain.*
import stainless.collection.List
import stainless.lang.*
import stainless.proof.*

object Properties:

  val brushAction           = new BrushAction
  val circleAction          = new CircleAction
  val rectangleAction       = new RectangleAction
  val eraserCanvasAction    = new EraserCanvasAction
  val overlaidEraserAction  = new OverlaidEraserAction
  val mouseDragUpdateAction = new MouseDragUpdateAction

  val drawActions = List(
    brushAction,
    circleAction,
    rectangleAction,
    eraserCanvasAction
  )

  val brushChain = new Chainable[DrawingModel, Cmd]:

    override def v1: Variation[DrawingModel, Cmd] = brushAction

    override def v2: Variation[DrawingModel, Cmd] = mouseDragUpdateAction

  val eraserChain = new Chainable[DrawingModel, Cmd]:

    override def v1: Variation[DrawingModel, Cmd] = eraserCanvasAction

    override def v2: Variation[DrawingModel, Cmd] = overlaidEraserAction

  /**
    * Property that checks that the result of RectangleAction behaves correctly for overlaid rectangles. Namely, it
    * checks that the overlaid rectangle will always be visible when the selected tool is a rectangle, and the mouse
    * dragging interaction is just starting. If the RectangleAction run function changes so that this property no longer
    * holds, Stainless will throw an error
    */
  def overlaidRectangleVisibility(
      previous: Cmd,
      mouseDown: MouseMsg.MouseDown,
      model: DrawingModel
  ): Boolean =
    val inputModel  = model.copy(
      selectedTool = Tool.Rectangle,
      receivedMessage = Msg.Mouse(mouseDown),
      mouseDragging = None()
    )
    val (_, result) = rectangleAction.run(previous, inputModel)
    Cmd.syntax.overlaidRectangle.isVisible(result).holds

  /**
    * Property that verifies the dependency between all the drawing actions and the mouseDragUpdateAction. The mouse
    * dragging update should always happen after any drawing action in the composition chain. We must ensure that the
    * activation predicates and state manipulation of the drawing actions are consistent by enforcing the implication
    * from the Chainable type
    */
  def drawActionChain(previous: Cmd, model: DrawingModel): Boolean =
    val msg             = model.receivedMessage
    val mouseMsgSubtype = (
      Msg.isMouseMove(msg) || Msg.isMouseUp(msg)
    ) ==> Msg.isMouseMsg(msg)

    drawActions.forall { action =>
      action.isActive(model) ==> {
        val (newModel, _) = action.run(previous, model)
        mouseDragUpdateAction.isActive(newModel)
      }.because(mouseMsgSubtype)
    }.holds

end Properties
