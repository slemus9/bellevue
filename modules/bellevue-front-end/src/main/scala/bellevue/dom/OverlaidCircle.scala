package bellevue.dom

import bellevue.domain.geometry.Circle
import bellevue.domain.geometry.Pixels.px
import cats.effect.IO
import cats.syntax.show.*
import org.scalajs.dom.HTMLElement

final class OverlaidCircle(element: HTMLElement) extends OverlaidShape(element):

  def draw(circle: Circle): IO[Unit] = IO:
    element.style.left = (circle.center.x - circle.radius).px.show
    element.style.top = (circle.center.y - circle.radius).px.show
    element.style.width = circle.diameter.show
    element.style.height = circle.diameter.show

object OverlaidCircle:

  def get(id: String): IO[OverlaidCircle] =
    getById[HTMLElement](id).map(OverlaidCircle.apply)
