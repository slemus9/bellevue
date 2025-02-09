package bellevue

import bellevue.commands.Command
import bellevue.domain.*
import bellevue.html.BellevueHtml
import bellevue.subscriptions.Subscription
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

    case ControlMsg.Partial(Left(error)) =>
      model -> Logger.error[IO](error)

    case ControlMsg.Partial(Right(msg)) =>
      update(model)(msg)

    case ControlMsg.HtmlElementLoaded(BellevueHtml.CanvasId) =>
      model -> Command.resizeCanvas

    case ControlMsg.ResizeCanvas =>
      model -> Command.resizeCanvas

    case msg: MouseMsg if model.selectedTool == Tool.Brush =>
      drawWithBrush(model)(msg)

    case msg: MouseMsg if model.selectedTool == Tool.Rectangle =>
      drawRectangle(model)(msg)

    case ToolboxMsg.PickColor(color) =>
      model.focus(_.brushConfig.color).replace(color) -> Cmd.None

    case ToolboxMsg.PickBrushSize(size) =>
      model.focus(_.brushConfig.lineWidth).replace(size) -> Cmd.None

    case ToolboxMsg.PickTool(tool) =>
      pickTool(model)(tool)

    case _ =>
      model -> Cmd.None

  end update

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

  private def drawWithBrush(model: DrawingModel): MouseMsg => (DrawingModel, Cmd[IO, Msg]) =
    case MouseMsg.MouseDown(from) =>
      model.copy(isDrawing = true, linePosition = from) -> Command.setLineStyle(model.brushConfig)

    case MouseMsg.MouseMove(to) if model.isDrawing =>
      model.copy(linePosition = to) -> Command.drawLineSegment(from = model.linePosition, to)

    case MouseMsg.MouseUp(to) =>
      model.copy(isDrawing = false) -> Command.drawLineSegment(from = model.linePosition, to)

    case _ => model -> Cmd.None

  private def drawRectangle(model: DrawingModel): MouseMsg => (DrawingModel, Cmd[IO, Msg]) =
    case MouseMsg.MouseDown(from) =>
      model.copy(isDrawing = true, rectangleStart = from) -> Command.setLineStyle(model.brushConfig)

    case MouseMsg.MouseMove(_) if model.isDrawing => model -> Cmd.None

    case MouseMsg.MouseUp(to) =>
      val from                   = model.rectangleStart
      val (topLeft, bottomRight) = if to.y < from.y then (from, to) else (to, from)

      model.copy(isDrawing = false) -> Command.drawRectangle(topLeft, bottomRight)

    case _ => model -> Cmd.None

  private def pickTool(model: DrawingModel): Tool => (DrawingModel, Cmd[IO, Msg]) =
    case Tool.Brush =>
      model.copy(selectedTool = Tool.Brush) -> Cmd.None

    case Tool.Eraser =>
      model.copy(selectedTool = Tool.Brush).focus(_.brushConfig.color).replace(BrushConfig.EraserColor) -> Cmd.None

    case Tool.Rectangle =>
      model.copy(selectedTool = Tool.Rectangle) -> Cmd.None

end Main
