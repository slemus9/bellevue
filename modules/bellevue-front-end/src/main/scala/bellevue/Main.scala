package bellevue

import bellevue.domain.*
import bellevue.domain.geometry.Pixels.px
import bellevue.domain.geometry.Point
import bellevue.html.BellevueHtml
import bellevue.logic.*
import bellevue.logic.BellevueAction
import bellevue.subscriptions.Subscription
import cats.effect.IO
import tyrian.*
import tyrian.cmds.Logger

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("BelleVueApp")
object Main extends TyrianIOApp[Msg, DrawingModel]:

  override def router: Location => Msg = Routing.none(ControlMsg.NoAction)

  override def init(flags: Map[String, String]): (DrawingModel, Cmd[IO, Msg]) =
    (DrawingModel.init, Cmd.None)

  override def update(model: DrawingModel): Msg => (DrawingModel, Cmd[IO, Msg]) =
    case Msg.Partial(Left(error)) => (model, Logger.error[IO](error.getMessage))
    case Msg.Partial(Right(msg))  => update(model)(msg)
    case DrawChart                => (model, ChartAction.draw(Point(500.px, 500.px)))
    case msg                      => BellevueAction.runWith(model.withMessage(msg), Cmd.None)

  override def view(model: DrawingModel): Html[Msg] =
    BellevueHtml.view(model)

  override def subscriptions(model: DrawingModel): Sub[IO, Msg] =
    Sub.Batch(
      Subscription.mouseDown,
      Subscription.mouseMove,
      Subscription.mouseUp,
      Subscription.resize,
      Subscription.waitForElement(BellevueHtml.CanvasId)
    )

end Main
