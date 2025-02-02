package bellevue.subscription

import bellevue.domain.{Msg, Point}
import cats.effect.IO
import org.scalajs.dom
import tyrian.Sub

object Subscription:

  val mouseDown: Sub[IO, Msg] =
    Sub.fromEvent("mousedown", dom.document):
      case event: dom.MouseEvent => Some(Msg.DrawLineStart(Point(event.clientX, event.clientY)))
      case _                     => None

  val mouseMove: Sub[IO, Msg] =
    Sub.fromEvent("mousemove", dom.document):
      case event: dom.MouseEvent => Some(Msg.DrawLineTo(Point(event.clientX, event.clientY)))
      case _                     => None

  val mouseUp: Sub[IO, Msg] =
    Sub.fromEvent("mouseup", dom.document):
      case event: dom.MouseEvent => Some(Msg.DrawLineEnd)
      case _                     => None
