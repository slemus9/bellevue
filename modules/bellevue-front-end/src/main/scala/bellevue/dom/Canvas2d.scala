package bellevue.dom

import bellevue.dom.actions.{castTo, throwError}
import bellevue.domain.geometry.*
import bellevue.domain.tools.StyleConfig
import cats.syntax.all.*
import org.scalajs.dom.{CanvasRenderingContext2D, Element}
import org.scalajs.dom.html.Canvas

/**
  * Represents an HTML canvas that we can use to draw 2D shapes
  */
final class Canvas2d private (
    var canvas: Canvas,
    var context: CanvasRenderingContext2D,
    var fillStyle: Option[RGB] = None
):

  /**
    * Finds the relative position of a given [[point]] in the window with respect to the canvas dimensions
    */
  def relativePositionOf(point: Point) = Point(
    x = math.floor(point.x - canvas.getBoundingClientRect().left),
    y = math.floor(point.y - canvas.getBoundingClientRect().top)
  )

  /**
    * Resize the canvas with respect to its parent. Useful for reorganizing the canvas content when the browser's window
    * is resized
    */
  def resize: Unit =
    val parentNode     = canvas.parentElement.castTo[Element]
    val parentBox      = parentNode.getBoundingClientRect()
    val currentDrawing = context.getImageData(0, 0, canvas.width - 1, canvas.height - 1)
    canvas.width = parentBox.width.toInt
    canvas.height = parentBox.height.toInt
    context.putImageData(currentDrawing, 0, 0)

  /**
    * Get the current image data of the entire area of the canvas
    */
  def getImage: Image =
    val imageData = context.getImageData(0, 0, canvas.width, canvas.height)
    Image.from(imageData.data, canvas.width, canvas.height).throwError

  def setStyle(config: StyleConfig): Unit =
    context.lineCap = "round"
    context.strokeStyle = config.color.toHexString
    context.lineWidth = config.lineWidth
    this.fillStyle = config.fillStyle
    if fillStyle.isDefined then context.fillStyle = fillStyle.get.toHexString

  def drawLineSegment(from: Point, to: Point): Unit =
    context.beginPath()
    context.moveTo(from.x, from.y)
    context.lineTo(to.x, to.y)
    context.stroke()

  def drawRectangle(rectangle: Rectangle): Unit =
    if this.fillStyle.isDefined then context.fill()
    context.strokeRect(
      x = rectangle.topLeft.x,
      y = rectangle.topLeft.y,
      w = rectangle.width,
      h = rectangle.height
    )

  def drawCircle(circle: Circle): Unit =
    context.beginPath()
    context.arc(
      x = circle.center.x,
      y = circle.center.y,
      circle.radius,
      startAngle = 0,
      endAngle = 2 * math.Pi
    )
    if this.fillStyle.isDefined then context.fill()
    context.stroke()

end Canvas2d

object Canvas2d:

  def make(id: String): Ref[Canvas2d] =
    Ref.make {
      val htmlCanvas = actions.getById[Canvas](id)
      Canvas2d(
        htmlCanvas,
        context = htmlCanvas.getContext("2d").castTo[CanvasRenderingContext2D]
      )
    } { canvas2d =>
      canvas2d.canvas = actions.getById[Canvas](id)
      canvas2d.context = canvas2d.canvas.getContext("2d").castTo[CanvasRenderingContext2D]
      canvas2d
    }
