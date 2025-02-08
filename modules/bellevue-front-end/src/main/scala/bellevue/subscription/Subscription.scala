package bellevue.subscription

import bellevue.domain.{Msg, Point}
import cats.effect.IO
import org.scalajs.dom
import tyrian.Sub

import scala.scalajs.js.Array

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

  val resize: Sub[IO, Msg] =
    Sub.fromEvent("resize", dom.window):
      case _ => Some(Msg.ResizeCanvas)

  def waitForElement(elementId: String): Sub[IO, Msg] =

    def observeElement(callback: Either[Throwable, Unit] => Unit): Unit =
      Option(dom.document.getElementById(elementId)) match
        case None    => ()
        case Some(_) => callback(Right(()))

    Sub.make[IO, Unit, Msg, dom.MutationObserver]("waitForElement") { callback =>
      IO:
        val options = new dom.MutationObserverInit {}
        options.childList = true
        options.subtree = true

        val observer = dom.MutationObserver((_, _) => observeElement(callback))
        observer.observe(dom.document, options)
        observer
    } { observer =>
      IO(observer.disconnect())
    } { _ => Some(Msg.LoadedElement(elementId)) }

end Subscription
