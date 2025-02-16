package bellevue

import bellevue.commands.Command
import bellevue.domain.*
import bellevue.domain.tools.Tool
import bellevue.html.BellevueHtml
import bellevue.logic.*
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

    case ControlMsg.Partial(Left(error)) =>
      (model, Logger.error[IO](error))

    case ControlMsg.Partial(Right(msg)) =>
      update(model)(msg)

    case ControlMsg.ResizeCanvas | ControlMsg.HtmlElementLoaded(BellevueHtml.CanvasId) =>
      (model, Command.resizeCanvas)

    case ControlMsg.MapToCanvas(point, buildMsg) =>
      (model, Command.mapToCanvasPosition(point).map(buildMsg))

    case msg: MouseMsg if model.selectedTool == Tool.Brush =>
      BrushAction.draw(model, msg)

    case msg: MouseMsg if model.selectedTool == Tool.Circle =>
      CircleAction.draw(model, msg)

    case msg: MouseMsg if model.selectedTool == Tool.Rectangle =>
      RectangleAction.draw(model, msg)

    case ToolboxMsg.PickColor(color) =>
      (ToolboxAction.pickColor(model, color), Cmd.None)

    case ToolboxMsg.PickBrushSize(size) =>
      (ToolboxAction.pickBrushSize(model, size), Cmd.None)

    case ToolboxMsg.PickTool(tool) =>
      (ToolboxAction.pickTool(model, tool), Cmd.None)

    case _ => (model, Cmd.None)

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
