package bellevue.dom

import bellevue.domain.geometry.Pixels.px
import cats.effect.IO
import cats.syntax.show.*
import org.scalajs.dom.HTMLElement

trait OverlaidShape(element: HTMLElement):

  val show: IO[Unit] = IO:
    element.style.visibility = "visible"
    element.style.borderWidth = 1.px.show

  val hide: IO[Unit] = IO:
    element.style.visibility = "hidden"
    element.style.borderWidth = 0.px.show
    element.style.width = 0.px.show
    element.style.height = 0.px.show
