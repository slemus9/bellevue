package bellevue.dom

import bellevue.domain.geometry.Pixels.px
import bellevue.domain.geometry.Rectangle
import cats.syntax.show.*
import org.scalajs.dom.HTMLElement

final class OverlaidRectangle(var element: HTMLElement) extends OverlaidShape(element):

  def draw(rectangle: Rectangle): Unit =
    element.style.left = rectangle.topLeft.x.px.show
    element.style.top = rectangle.topLeft.y.px.show
    element.style.width = rectangle.width.px.show
    element.style.height = rectangle.height.px.show

object OverlaidRectangle:

  def make(id: String): Ref[OverlaidRectangle] =
    Ref.make {
      OverlaidRectangle(actions.getById[HTMLElement](id))
    } { overlaidRectangle =>
      overlaidRectangle.element = actions.getById[HTMLElement](id)
      overlaidRectangle
    }
