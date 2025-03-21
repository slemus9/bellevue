package bellevue.dom

import bellevue.domain.geometry.Pixels.px
import cats.syntax.show.*
import org.scalajs.dom.HTMLElement

/**
  * Represents a shape that can be overlaid on top of other HTML elements. This can be useful, for example, when we want
  * to display a rectangle when performing a selection over a drawing in the canvas
  */
trait OverlaidShape(element: HTMLElement):

  def show: Unit =
    element.style.visibility = "visible"
    element.style.borderWidth = 1.px.show

  def hide: Unit =
    element.style.visibility = "hidden"
    element.style.borderWidth = 0.px.show
    element.style.width = 0.px.show
    element.style.height = 0.px.show
