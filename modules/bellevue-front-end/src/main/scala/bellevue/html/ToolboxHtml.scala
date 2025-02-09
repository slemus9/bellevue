package bellevue.html

import bellevue.domain.*
import cats.syntax.show.*
import tyrian.Html
import tyrian.Html.*

final class ToolboxHtml(model: DrawingModel):

  lazy val view: Html[Msg] =
    div(id := "toolbox", className := "toolbox-container")(
      (viewBrushColorInput
        ++ viewBrushSizeInput
        ++ List(viewEraserButton))*
    )

  private lazy val viewBrushColorInput: List[Html[Msg]] =
    List(
      span("Brush Color: "),
      input(
        `type` := "color",
        value  := model.brushConfig.color,
        onChange(ToolboxMsg.PickColor.apply)
      )
    )

  private lazy val viewBrushSizeInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      ControlMsg.Partial(Pixels.parse(str).map(ToolboxMsg.PickBrushSize.apply))

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

  private lazy val viewEraserButton: Html[Msg] =
    button(onClick(EraserMsg.Enable))("Eraser")
