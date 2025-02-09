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

  override def router: Location => Msg = Routing.none(ControlMsg.NoAction)

  override def init(flags: Map[String, String]): (DrawingModel, Cmd[IO, Msg]) =
    DrawingModel.init -> Cmd.None

  override def update(model: DrawingModel): Msg => (DrawingModel, Cmd[IO, Msg]) =

    case BrushMsg.Start(from) =>
      model.copy(linePosition = from, isDrawingLine = true) -> Command.setLineStyle(model.brushConfig)

    case BrushMsg.To(to) if model.isDrawingLine =>
      model.copy(linePosition = to) -> Command.drawLineSegment(
        from = model.linePosition,
        to = to
      )

    case BrushMsg.End =>
      model.copy(isDrawingLine = false) -> Cmd.None

    case ControlMsg.Partial(Left(error)) =>
      model -> Logger.error[IO](error)

    case ControlMsg.Partial(Right(msg)) =>
      update(model)(msg)

    case ControlMsg.HtmlElementLoaded(BellevueHtml.CanvasId) =>
      model -> Command.resizeCanvas

    case ControlMsg.ResizeCanvas =>
      model -> Command.resizeCanvas

    case EraserMsg.Enable =>
      model.focus(_.brushConfig.color).replace(BrushConfig.EraserColor) -> Cmd.None

    case ToolboxMsg.PickColor(color) =>
      model.focus(_.brushConfig.color).replace(color) -> Cmd.None

    case ToolboxMsg.PickBrushSize(size) =>
      model.focus(_.brushConfig.lineWidth).replace(size) -> Cmd.None

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
