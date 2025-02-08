package bellevue.html

import bellevue.domain.*
import cats.syntax.show.*
import tyrian.Html
import tyrian.Html.*

final class ToolboxHtml(model: DrawingModel):

  lazy val view: Html[Msg] =
    div(id := "toolbox", className := "toolbox-container")(
      (viewBrushColorInput ++ viewBrushSizeInput)*
    )

  private lazy val viewBrushColorInput: List[Html[Msg]] =
    List(
      span("Brush Color: "),
      input(
        `type` := "color",
        value  := model.brushConfig.color,
        onChange(Msg.PickColor.apply)
      )
    )

  private lazy val viewBrushSizeInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      Msg.Partial(Pixels.parse(str).map(Msg.PickBrushSize.apply))

    List(
      span("Brush Size: "),
      input(
        list  := "brush-size-options",
        value := model.brushConfig.lineWidth.show,
        onChange(buildMessage)
      ),
      datalist(id := "brush-size-options")(
        BrushConfig.lineWidths.map(size => option(size.show))
      )
    )
