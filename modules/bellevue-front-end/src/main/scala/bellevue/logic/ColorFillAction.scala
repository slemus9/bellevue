package bellevue.logic

import bellevue.domain.*
import bellevue.domain.geometry.*
import bellevue.domain.geometry.Pixels.px
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
        Cmd.Run:
          val origin @ (x, y) = (point.x.toInt, point.y.toInt)
          println(s"Flood from $point")
          image.get(x, y) match
            case Some(originColor) =>
              canvas.flatMap { canvas =>
                IO(println("Got canvas!")) >> IO:
                  exploreImage(image, originColor, origin) { (x, y) =>
                    val pixelArea = Rectangle(Point(x.px, y.px), Point((x + 1).px, (y + 1).px))
                    canvas.unsafeDrawRectangle(pixelArea)
                  }
              } as ControlMsg.NoAction

            case None => IO.pure(ControlMsg.NoAction)

  private def exploreImage(image: Image, color: RGBA, origin: Position)(f: Position => Unit): Unit =
    println("Starting Exploration")
    val q       = mutable.Queue(origin)
    val visited = mutable.Set(origin)

    while q.nonEmpty do
      val v = q.dequeue()
      for w @ (x, y) <- neighbors(v) do
        if !visited(w) && image.get(x, y).exists(_ == color) then
          visited += (w)
          q.enqueue(w)
          f(w)

  private val neighbors: Position => List[Position] = (x, y) =>
    List(
      (x, y - 1),
      (x + 1, y),
      (x, y + 1),
      (x - 1, y)
    )

  type Position = (Int, Int)
end ColorFillAction
