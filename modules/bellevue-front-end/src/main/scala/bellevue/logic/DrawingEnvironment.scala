package bellevue.logic

import bellevue.dom.*
import bellevue.html.BellevueHtml
import cats.effect.IO
import tyrian.Cmd

/**
  * Centralizes all the common elements that we need in order to run drawing commands in our application
  */
trait DrawingEnvironment:

  protected val canvas: IO[Canvas2d] =
    Canvas2d.get(BellevueHtml.CanvasId)

  protected val overlaidRectangle: IO[OverlaidRectangle] =
    OverlaidRectangle.get(BellevueHtml.OverlaidRectangleId)

  protected val overlaidCircle: IO[OverlaidCircle] =
    OverlaidCircle.get(BellevueHtml.OverlaidCircleId)

  extension [A](action: IO[A])

    protected def command: Cmd[IO, A] =
      Cmd.Run(action)

    protected def run(f: A => IO[Unit]): Cmd[IO, Nothing] =
      Cmd.SideEffect(action.flatMap(f))
