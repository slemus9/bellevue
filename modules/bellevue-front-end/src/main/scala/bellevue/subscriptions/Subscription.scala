package bellevue.subscriptions

import bellevue.domain.*
import cats.effect.IO
import org.scalajs.dom
import tyrian.Sub

import scala.scalajs.js.Array

object Subscription:

  val mouseDown: Sub[IO, Msg] =
    Sub.fromEvent("mousedown", dom.document):
      case event: dom.MouseEvent => Some(MouseMsg.MouseDown(Point(event.clientX, event.clientY)))
      case _                     => None

  val mouseMove: Sub[IO, Msg] =
    Sub.fromEvent("mousemove", dom.document):
      case event: dom.MouseEvent => Some(MouseMsg.MouseMove(Point(event.clientX, event.clientY)))
      case _                     => None

  val mouseUp: Sub[IO, Msg] =
    Sub.fromEvent("mouseup", dom.document):
      case event: dom.MouseEvent => Some(MouseMsg.MouseUp(Point(event.clientX, event.clientY)))
      case _                     => None

  val resize: Sub[IO, Msg] =
    Sub.fromEvent("resize", dom.window):
      case _ => Some(ControlMsg.ResizeCanvas)

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
    } { _ => Some(ControlMsg.HtmlElementLoaded(elementId)) }

end Subscription
