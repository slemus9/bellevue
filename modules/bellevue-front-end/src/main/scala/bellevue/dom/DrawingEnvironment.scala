package bellevue.dom

import bellevue.html.*
import cats.effect.IO
import tyrian.Cmd

/**
  * Centralizes all the common elements that we need in order to run drawing commands in our application
  */
trait DrawingEnvironment:

  def command[Msg](run: DrawingElements => Msg): Cmd[IO, Msg] =
    Cmd.Run(IO(run(DrawingElements.ref)))

  def sideEffect(run: DrawingElements => Unit): Cmd[IO, Nothing] =
    Cmd.SideEffect(run(DrawingElements.ref))

final class DrawingElements(
    val canvas: Ref[Canvas2d],
    val overlaidCircle: Ref[OverlaidCircle],
    val overlaidRectangle: Ref[OverlaidRectangle],
    val toolboxElement: Ref[ToolboxElement]
)

object DrawingElements:

  val ref = DrawingElements(
    canvas = Canvas2d.make(BellevueHtml.CanvasId),
    overlaidCircle = OverlaidCircle.make(BellevueHtml.OverlaidCircleId),
    overlaidRectangle = OverlaidRectangle.make(BellevueHtml.OverlaidRectangleId),
    toolboxElement = ToolboxElement.make(ToolboxHtml.ToolboxElementId)
  )
