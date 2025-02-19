package bellevue.logic

import bellevue.dom.*
import bellevue.html.BellevueHtml
import cats.effect.IO

/**
  * Common elements that we need to get in order to run commands
  */
object Get:

  val canvas: IO[Canvas2d] =
    Canvas2d.get(BellevueHtml.CanvasId)

  val overlaidRectangle: IO[OverlaidRectangle] =
    OverlaidRectangle.get(BellevueHtml.OverlaidRectangleId)

  val overlaidCircle: IO[OverlaidCircle] =
    OverlaidCircle.get(BellevueHtml.OverlaidCircleId)
