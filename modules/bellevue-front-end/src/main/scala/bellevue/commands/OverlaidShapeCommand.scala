package bellevue.commands

import bellevue.domain.Point
import bellevue.html.BellevueHtml
import cats.effect.IO
import org.scalajs.dom
import tyrian.Cmd

trait OverlaidShapeCommand:

  def placeOverlaidRectange(topLeft: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val div = getOverlaidRectangle
      div.style.visibility = "visible"
      div.style.left = s"${topLeft.x}px"
      div.style.top = s"${topLeft.y}px"

  def growOverlaidRectangle(topLeft: Point, bottomRight: Point): Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val div = getOverlaidRectangle
      dom.console.log(s"${bottomRight.y - topLeft.y}px")
      div.style.width = s"${(bottomRight.x - topLeft.x).abs}px"
      div.style.height = s"${(bottomRight.y - topLeft.y).abs}px"

  val hideOverlaidRectangle: Cmd[IO, Nothing] =
    Cmd.SideEffect:
      val div = getOverlaidRectangle
      div.style.visibility = "hidden"
      div.style.width = s"0px"
      div.style.height = s"0px"

  private def getOverlaidRectangle: dom.HTMLElement =
    dom.document.getElementById(BellevueHtml.OverlaidRectangleId).asInstanceOf[dom.HTMLElement]
