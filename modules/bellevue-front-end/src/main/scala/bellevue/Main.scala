package bellevue

import bellevue.commands.Command
import bellevue.domain.*
import bellevue.html.BellevueHtml
import bellevue.subscription.Subscription
import cats.effect.IO
import tyrian.*

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("BelleVueApp")
object Main extends TyrianIOApp[Msg, DrawingModel]:

  override def router: Location => Msg = Routing.none(Msg.NoAction)

  override def init(flags: Map[String, String]): (DrawingModel, Cmd[IO, Msg]) =
    DrawingModel.init -> Cmd.None

  override def update(model: DrawingModel): Msg => (DrawingModel, Cmd[IO, Msg]) =
    case Msg.LoadedElement(DrawingModel.CanvasId) =>
      model -> Command.resizeCanvas

    case Msg.ResizeCanvas =>
      model -> Command.resizeCanvas

    case Msg.PickColor(color) =>
      model.setLineColor(color) -> Cmd.None

    case Msg.DrawLineStart(from) =>
      model.updateLinePosition(from).enableLineDrawing -> Command.setLineStyle(model)

    case Msg.DrawLineTo(to) if model.isDrawingLine =>
      model.updateLinePosition(to) -> Command.drawLineSegment(
        from = model.linePosition,
        to = to
      )

    case Msg.DrawLineEnd =>
      model.disableLineDrawing -> Cmd.None

    case _ =>
      model -> Cmd.None

  override def view(model: DrawingModel): Html[Msg] =
    BellevueHtml.view(model)

  override def subscriptions(model: DrawingModel): Sub[IO, Msg] =
    Sub.Batch(
      Subscription.mouseDown,
      Subscription.mouseMove,
      Subscription.mouseUp,
      Subscription.resize,
      Subscription.waitForElement(DrawingModel.CanvasId)
    )

end Main
