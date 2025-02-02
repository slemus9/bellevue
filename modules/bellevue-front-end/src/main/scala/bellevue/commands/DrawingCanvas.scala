package bellevue.commands

import bellevue.domain.Point
import org.scalajs.dom.document
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.CanvasRenderingContext2D

opaque type DrawingCanvas <: Canvas = Canvas

object DrawingCanvas:

  val id = "drawing-canvas"

  def get: DrawingCanvas =
    document.getElementById(id).asInstanceOf[Canvas]

  extension (canvas: DrawingCanvas)
    def context2d = canvas.getContext("2d").asInstanceOf[CanvasRenderingContext2D]

    def relativePosition(p: Point) =
      Point(
        math.floor(p.x - canvas.getBoundingClientRect().left),
        math.floor(p.y - canvas.getBoundingClientRect().top)
      )
