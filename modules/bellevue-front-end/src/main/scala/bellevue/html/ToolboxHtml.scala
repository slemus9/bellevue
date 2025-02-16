package bellevue.html

import bellevue.domain.*
import bellevue.domain.geometry.Pixels
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
          viewRectangleButton,
          viewEraserButton
        ))*
    )

  private lazy val viewBrushColorInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      ControlMsg.Partial(Color.parse(str).map(ToolboxMsg.PickColor.apply))

    List(
      span("Brush Color: "),
      input(
        `type` := "color",
        value  := model.lineConfig.color,
        onChange(buildMessage)
      )
    )

  private lazy val viewBrushSizeInput: List[Html[Msg]] =
    def buildMessage(str: String) =
      ControlMsg.Partial(Pixels.parse(str).map(ToolboxMsg.PickBrushSize.apply))

    List(
      span("Brush Size: "),
      input(
        list  := "brush-size-options",
        value := model.lineConfig.lineWidth.show,
        onChange(buildMessage)
      ),
      datalist(id := "brush-size-options")(
        LineConfig.LineWidths.map(size => option(size.show))
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
