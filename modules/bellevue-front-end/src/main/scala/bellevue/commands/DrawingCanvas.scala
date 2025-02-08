package bellevue.commands

import bellevue.domain.Point
import bellevue.html.BellevueHtml
import org.scalajs.dom.{document, CanvasRenderingContext2D}
import org.scalajs.dom.html.Canvas

opaque type DrawingCanvas <: Canvas = Canvas

object DrawingCanvas:

  def get: DrawingCanvas =
    document.getElementById(BellevueHtml.CanvasId).asInstanceOf[Canvas]

  extension (canvas: DrawingCanvas)
    def context2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    def relativePosition(p: Point) =
      Point(
        math.floor(p.x - canvas.getBoundingClientRect().left),
        math.floor(p.y - canvas.getBoundingClientRect().top)
      )
