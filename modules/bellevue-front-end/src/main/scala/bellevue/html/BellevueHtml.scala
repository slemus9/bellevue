package bellevue.html

import bellevue.domain.*
import tyrian.Html
import tyrian.Html.*

object BellevueHtml:

  val CanvasId            = "drawing-canvas"
  val OverlaidRectangleId = "overlaid-rectangle"

  def view(model: DrawingModel): Html[Msg] =
    div(id := "bellevue", className := "bellevue-container")(
      canvas(id := CanvasId, className := "drawing-canvas")(),
      ToolboxHtml(model).view,
      viewOverlaidRectangle
    )

  lazy val viewOverlaidRectangle: Html[Msg] =
    div(id := OverlaidRectangleId, className := "overlaid-shape")()
