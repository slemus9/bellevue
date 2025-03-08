package bellevue.dom

import bellevue.domain.geometry.*
import bellevue.domain.geometry.Pixels.px
import bellevue.domain.tools.{RGB, StyleConfig}
import cats.effect.IO
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
    x = math.floor(point.x - canvas.getBoundingClientRect().left).px,
    y = math.floor(point.y - canvas.getBoundingClientRect().top).px
  )

  /**
    * Resize the canvas with respect to its parent. Useful for reorganizing the canvas content when the browser's window
    * is resized
    */
  val resize: IO[Unit] =
    canvas.parentNode.as[Element].liftTo[IO].flatMap { parentNode =>
      IO:
        val parentBox      = parentNode.getBoundingClientRect()
        val currentDrawing = context.getImageData(0, 0, canvas.width - 1, canvas.height - 1)
        canvas.width = parentBox.width.toInt
        canvas.height = parentBox.height.toInt
        context.putImageData(currentDrawing, 0, 0)
    }

  def setStyle(config: StyleConfig): IO[Unit] = IO:
    context.lineCap = "round"
    context.strokeStyle = config.color.toHexString
    context.lineWidth = config.lineWidth
    this.fillStyle = config.fillStyle
    this.fillStyle.fold(()): fillStyle =>
      context.fillStyle = fillStyle.toHexString

  def drawLineSegment(from: Point, to: Point): IO[Unit] = IO:
    context.beginPath()
    context.moveTo(from.x, from.y)
    context.lineTo(to.x, to.y)
    context.stroke()

  def drawRectangle(rectangle: Rectangle): IO[Unit] = IO:
    if this.fillStyle.isDefined then context.fill()
    context.strokeRect(
      x = rectangle.topLeft.x,
      y = rectangle.topLeft.y,
      w = rectangle.width,
      h = rectangle.height
    )

  def drawCircle(circle: Circle): IO[Unit] = IO:
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

  private var ref: Option[Canvas2d] = None

  def get(id: String): IO[Canvas2d] =
    for
      htmlCanvas <- getById[Canvas](id)
      context2d  <- htmlCanvas.getContext("2d").as[CanvasRenderingContext2D].liftTo[IO]
      canvas2d   <- IO(update(htmlCanvas, context2d))
    yield canvas2d

  private def update(htmlCanvas: Canvas, context: CanvasRenderingContext2D): Canvas2d =
    ref match
      case None =>
        val canvas2d = new Canvas2d(htmlCanvas, context)
        ref = Some(canvas2d)
        canvas2d

      case Some(canvas2d) =>
        canvas2d.canvas = htmlCanvas
        canvas2d.context = context
        canvas2d
