package bellevue.dom

import bellevue.domain.geometry.Pixels.px
import bellevue.domain.geometry.Rectangle
import cats.effect.IO
import cats.syntax.show.*
import org.scalajs.dom.HTMLElement

final class OverlaidRectangle(element: HTMLElement) extends OverlaidShape(element):

  def draw(rectangle: Rectangle): IO[Unit] = IO:
    element.style.left = rectangle.topLeft.x.px.show
    element.style.top = rectangle.topLeft.y.px.show
    element.style.width = rectangle.width.px.show
    element.style.height = rectangle.height.px.show

object OverlaidRectangle:

  def get(id: String): IO[OverlaidRectangle] =
    getById[HTMLElement](id).map(OverlaidRectangle.apply)
