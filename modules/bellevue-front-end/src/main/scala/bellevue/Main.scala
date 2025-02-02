package bellevue

import bellevue.commands.*
import bellevue.domain.*
import bellevue.subscription.Subscription
import cats.effect.IO
import tyrian.*
import tyrian.Html.*

import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("BelleVueApp")
object Main extends TyrianIOApp[Msg, DrawingModel]:

  override def router: Location => Msg = Routing.none(Msg.Noop)

  override def init(flags: Map[String, String]): (DrawingModel, Cmd[IO, Msg]) =
    DrawingModel.init -> Cmd.None

  override def update(model: DrawingModel): Msg => (DrawingModel, Cmd[IO, Msg]) =
    case Msg.DrawLineStart(from) =>
      model.updateLinePosition(from).enableLineDrawing -> Command.initLineDrawing(
        color = "green",
        lineWidth = 2
      )

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
    div(id := "bellevue")(
      canvas(
        id     := DrawingCanvas.id,
        width  := 500,
        height := 300,
        style  := "border:1px solid #000000;"
      )()
    )

  override def subscriptions(model: DrawingModel): Sub[IO, Msg] =
    Sub.Batch(
      Subscription.mouseDown,
      Subscription.mouseMove,
      Subscription.mouseUp
    )

end Main
