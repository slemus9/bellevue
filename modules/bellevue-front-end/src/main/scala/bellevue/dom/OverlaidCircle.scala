package bellevue.dom

import bellevue.domain.geometry.Circle
import bellevue.domain.geometry.Pixels.px
import cats.syntax.show.*
import org.scalajs.dom.HTMLElement

final class OverlaidCircle(var element: HTMLElement) extends OverlaidShape(element):

  def draw(circle: Circle): Unit =
    element.style.left = (circle.center.x - circle.radius).px.show
    element.style.top = (circle.center.y - circle.radius).px.show
    element.style.width = circle.diameter.px.show
    element.style.height = circle.diameter.px.show

object OverlaidCircle:

  def make(id: String): Ref[OverlaidCircle] =
    Ref.make {
      OverlaidCircle(actions.getById[HTMLElement](id))
    } { overlaidCircle =>
      overlaidCircle.element = actions.getById[HTMLElement](id)
      overlaidCircle
    }
