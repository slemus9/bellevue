package bellevue.logic

import bellevue.dom.{Canvas2d, DrawingEnvironment}
import bellevue.domain.*
import bellevue.domain.geometry.*
import bellevue.domain.tools.Tool
import bellevue.logic.context.{Behavior, Variation}
import cats.effect.IO
import tyrian.Cmd

import scala.collection.mutable

object ColorFillAction extends Variation.Monoidal[DrawingModel, Cmd[IO, Msg]], DrawingEnvironment:

  override def isActive(state: DrawingModel): Boolean =
    state.selectedTool == Tool.ColorFill &&
      state.receivedMessage.isInstanceOf[MouseMsg] &&
      state.colorFillConfig.canvasImage.isDefined

  override val run: Behavior[DrawingModel, Cmd[IO, Msg]] = partialExecAndMerge: model =>
    (model.receivedMessage, model.colorFillConfig.canvasImage) match
      case (MouseMsg.MouseDown(point), Some(image)) =>
        command { elems =>
          val originPosition @ (x, y) = (point.x.toInt, point.y.toInt)
          image(x, y) match
            case Some(originColor) =>
              val canvas = elems.canvas.refresh
              floodFill(image, originColor, originPosition, canvas)
              ToolboxMsg.SetCanvasImage(canvas.getImage)

            case None => ControlMsg.NoAction
        }

  private def floodFill(
      image: Image,
      originColor: RGBA,
      originPosition: Position,
      canvas: Canvas2d
  ): Unit =
    exploreImage(image, originColor, originPosition) { (x, y) =>
      val pixelArea = Rectangle(Point(x, y), Point(x + 1, y + 1))
      canvas.drawRectangle(pixelArea)
    }

  private def exploreImage(image: Image, originColor: RGBA, originPosition: Position)(f: Position => Unit): Unit =
    val toExplore = mutable.Queue(originPosition)
    val visited   = mutable.Set(originPosition)

    while toExplore.nonEmpty do
      val current = toExplore.dequeue()
      for pos <- neighbors(current) do
        val (x, y) = pos
        if !visited(pos) && image(x, y).exists(_ == originColor) then
          visited += (pos)
          toExplore.enqueue(pos)
          f(pos)

  private val neighbors: Position => List[Position] = (x, y) =>
    List(
      (x, y - 1),
      (x + 1, y),
      (x, y + 1),
      (x - 1, y)
    )

  type Position = (Int, Int)

end ColorFillAction
