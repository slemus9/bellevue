package bellevue

import bellevue.commands.Command
import bellevue.domain.*
import bellevue.html.BellevueHtml
import bellevue.subscription.Subscription
import cats.effect.IO
import monocle.syntax.all.*
import tyrian.*
import tyrian.cmds.Logger

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("BelleVueApp")
object Main extends TyrianIOApp[Msg, DrawingModel]:

  override def router: Location => Msg = Routing.none(Msg.NoAction)

  override def init(flags: Map[String, String]): (DrawingModel, Cmd[IO, Msg]) =
    DrawingModel.init -> Cmd.None

  override def update(model: DrawingModel): Msg => (DrawingModel, Cmd[IO, Msg]) =

    case Msg.Partial(Left(error)) =>
      model -> Logger.error[IO](error)

    case Msg.Partial(Right(msg)) =>
      model -> Cmd.Run(IO.pure(msg))

    case Msg.LoadedElement(BellevueHtml.CanvasId) =>
      model -> Command.resizeCanvas

    case Msg.ResizeCanvas =>
      model -> Command.resizeCanvas

    case Msg.PickColor(color) =>
      model.focus(_.brushConfig.color).replace(color) -> Cmd.None

    case Msg.PickBrushSize(size) =>
      model.focus(_.brushConfig.lineWidth).replace(size) -> Cmd.None

    case Msg.DrawLineStart(from) =>
      model.copy(linePosition = from, isDrawingLine = true) -> Command.setLineStyle(model.brushConfig)

    case Msg.DrawLineTo(to) if model.isDrawingLine =>
      model.copy(linePosition = to) -> Command.drawLineSegment(
        from = model.linePosition,
        to = to
      )

    case Msg.DrawLineEnd =>
      model.copy(isDrawingLine = false) -> Cmd.None

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
      Subscription.waitForElement(BellevueHtml.CanvasId)
    )

end Main
