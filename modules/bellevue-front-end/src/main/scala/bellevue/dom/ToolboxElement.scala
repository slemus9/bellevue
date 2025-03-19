package bellevue.dom

import bellevue.domain.geometry.Point
import cats.effect.IO
import org.scalajs.dom.HTMLElement

final class ToolboxElement(element: HTMLElement):

  def contains(point: Point): Boolean =
    val area = element.getBoundingClientRect()
    area.left <= point.x && point.x <= area.left + area.width &&
    area.top <= point.y && point.y <= area.top + area.height

object ToolboxElement:

  def get(id: String): IO[ToolboxElement] =
    getById[HTMLElement](id).map(ToolboxElement.apply)
