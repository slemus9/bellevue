package bellevue.dom

import bellevue.domain.geometry.Point
import org.scalajs.dom.HTMLElement

/**
  * Encapsulates the element that holds everything related to the app's toolbox
  */
final class ToolboxElement(var element: HTMLElement):

  def contains(point: Point): Boolean =
    val area = element.getBoundingClientRect()
    area.left <= point.x && point.x <= area.left + area.width &&
    area.top <= point.y && point.y <= area.top + area.height

object ToolboxElement:

  def make(id: String): Ref[ToolboxElement] =
    Ref.make {
      ToolboxElement(actions.getById[HTMLElement](id))
    } { toolboxElement =>
      toolboxElement.element = actions.getById[HTMLElement](id)
      toolboxElement
    }
