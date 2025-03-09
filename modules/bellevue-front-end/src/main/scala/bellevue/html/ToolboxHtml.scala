package bellevue.html

import bellevue.domain.*
import bellevue.domain.geometry.*
import bellevue.domain.tools.*
import cats.syntax.show.*
import tyrian.Html
import tyrian.Html.*

final class ToolboxHtml(model: DrawingModel):

  lazy val view: Html[Msg] =
    div(id := "toolbox", className := "toolbox-container")(
      (viewBrushColorInput
        ++ viewBrushSizeInput
        ++ List(
          viewBrushButton,
          viewCircleButton,
          viewRectangleButton
        )
        ++ viewEraserRadiusInput
        ++ List(viewEraserButton))*
    )

  private lazy val viewBrushColorInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      Msg.Partial(RGB.parse(str).map(ToolboxMsg.PickColor.apply))

    List(
      span("Brush Color: "),
      input(
        `type` := "color",
        value  := model.brushConfig.color.toHexString,
        onChange(buildMessage)
      )
    )

  private lazy val viewBrushSizeInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      Msg.Partial(Pixels.parse(str).map(ToolboxMsg.PickBrushSize.apply))

    List(
      span("Brush Size: "),
      input(
        list  := "brush-size-options",
        value := model.brushConfig.lineWidth.show,
        onChange(buildMessage)
      ),
      datalist(id := "brush-size-options")(
        BrushConfig.LineWidths.map(size => option(size.show))
      )
    )

  private lazy val viewEraserRadiusInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      Msg.Partial(Pixels.parse(str).map(ToolboxMsg.PickEraserRadius.apply))

    List(
      span("Eraser Radius: "),
      input(
        list  := "eraser-radius-options",
        value := model.eraserConfig.radius.show,
        onChange(buildMessage)
      ),
      datalist(id := "eraser-radius-options")(
        EraserConfig.EraserRadius.map(size => option(size.show))
      )
    )

  private lazy val viewBrushButton: Html[Msg] =
    button(onClick(ToolboxMsg.PickTool(Tool.Brush)))("Brush")

  private lazy val viewCircleButton: Html[Msg] =
    button(onClick(ToolboxMsg.PickTool(Tool.Circle)))("Circle")

  private lazy val viewRectangleButton: Html[Msg] =
    button(onClick(ToolboxMsg.PickTool(Tool.Rectangle)))("Rectangle")

  private lazy val viewEraserButton: Html[Msg] =
    button(onClick(ToolboxMsg.PickTool(Tool.Eraser)))("Eraser")

end ToolboxHtml
