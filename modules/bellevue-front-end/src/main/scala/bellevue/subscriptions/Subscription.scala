package bellevue.subscriptions

import bellevue.domain.*
import bellevue.domain.geometry.Pixels.px
import bellevue.domain.geometry.Point
import cats.effect.IO
import org.scalajs.dom
import tyrian.Sub

import scala.scalajs.js.Array

object Subscription:

  val mouseDown: Sub[IO, Msg] =
    Sub.fromEvent("mousedown", dom.document):
      case event: dom.MouseEvent => Some(ControlMsg.MapToCanvas(mousePosition(event), MouseMsg.MouseDown.apply))
      case _                     => None

  val mouseMove: Sub[IO, Msg] =
    Sub.fromEvent("mousemove", dom.document):
      case event: dom.MouseEvent => Some(ControlMsg.MapToCanvas(mousePosition(event), MouseMsg.MouseMove.apply))
      case _                     => None

  val mouseUp: Sub[IO, Msg] =
    Sub.fromEvent("mouseup", dom.document):
      case event: dom.MouseEvent => Some(ControlMsg.MapToCanvas(mousePosition(event), MouseMsg.MouseUp.apply))
      case _                     => None

  val resize: Sub[IO, Msg] =
    Sub.fromEvent("resize", dom.window): _ =>
      Some(ControlMsg.ResizeCanvas)

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
    } { _ =>
      Some(ControlMsg.HtmlElementLoaded(elementId))
    }

  private def mousePosition(event: dom.MouseEvent): Point =
    Point(event.clientX.px, event.clientY.px)

end Subscription
