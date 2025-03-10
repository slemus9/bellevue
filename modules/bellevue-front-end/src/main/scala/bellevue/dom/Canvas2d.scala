package bellevue.dom

import bellevue.domain.geometry.{*, given}
import bellevue.domain.geometry.Pixels.px
import bellevue.domain.tools.LineConfig
import cats.effect.IO
import cats.syntax.all.*
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.CanvasRenderingContext2D
import org.scalajs.dom.Element

/**
  * Represents an HTML canvas that we can use to draw 2D shapes
  */
final class Canvas2d private (
    canvas: Canvas,
    context: CanvasRenderingContext2D
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

  def setLineStyle(config: LineConfig): IO[Unit] = IO:
    context.lineCap = "round"
    context.strokeStyle = config.color
    context.lineWidth = config.lineWidth

  def drawLineSegment(from: Point, to: Point): IO[Unit] = IO:
    context.beginPath()
    context.moveTo(from.x, from.y)
    context.lineTo(to.x, to.y)
    context.stroke()

  def drawRectangle(rectangle: Rectangle): IO[Unit] = IO:
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
    context.stroke()

  import bellevue.domain.tools.Color
  import cats.effect.std.Random
  import cats.syntax.all.*
  import io.github.iltotore.iron.autoRefine

  def drawChart(center: Point): IO[Unit] =
    val origin = relativePositionOf(center)

    val gridStyle = LineConfig(color = Color("#a6a6a6"), lineWidth = 1.px)
    val lineStyle = LineConfig(color = Color("#1a1aff"), lineWidth = 1.px)
    val axisStyle = LineConfig(color = Color("#000000"), lineWidth = 2.px)

    val xs = List.iterate(origin.x, len = 21)(_ + 20)
    val ys = List.iterate(origin.y, len = 21)(_ - 20)

    val horizontalGrid = ys.map(y => (Point(origin.x, y), Point(origin.x + 400, y)))
    val verticalGrid   = xs.map(x => (Point(x, origin.y), Point(x, origin.y - 400)))

    val drawAxis: IO[Unit] =
      setLineStyle(axisStyle) >>
        drawLineSegment(origin, Point(origin.x, origin.y - 400)) >>
        drawLineSegment(origin, Point(origin.x + 400, origin.y))

    val drawGrid: IO[Unit] =
      setLineStyle(gridStyle) >>
        horizontalGrid.traverse_(drawLineSegment) >>
        verticalGrid.traverse_(drawLineSegment)

    val drawLine: IO[Unit] =
      for
        random <- Random.scalaUtilRandom[IO]
        _      <- setLineStyle(lineStyle)
        points <- xs.traverse { x =>
                    random.elementOf(ys).map(y => Point(x, y))
                  }
        _      <- points.zip(points.tail).traverse_(drawLineSegment)
        _      <- points.traverse_ { point =>
                    drawCircle(Circle(center = point, radius = 2.px))
                  }
      yield ()

    drawAxis >> drawGrid >> drawLine

  end drawChart

end Canvas2d

object Canvas2d:

  def get(id: String): IO[Canvas2d] =
    for
      canvas    <- getById[Canvas](id)
      context2d <- canvas.getContext("2d").as[CanvasRenderingContext2D].liftTo[IO]
    yield Canvas2d(canvas, context2d)
