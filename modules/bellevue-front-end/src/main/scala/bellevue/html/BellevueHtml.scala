package bellevue.html

import bellevue.domain.*
import tyrian.Html
import tyrian.Html.*

object BellevueHtml:

  val CanvasId            = "drawing-canvas"
  val OverlaidRectangleId = "overlaid-rectangle"
  val OverlaidCircleId    = "overlaid-circle"

  def view(model: DrawingModel): Html[Msg] =
    div(id := "bellevue", className := "bellevue-container")(
      canvas(id := CanvasId, className := "drawing-canvas")(),
      ToolboxHtml(model).view,
      viewOverlaidRectangle,
      viewOverlaidCircle
    )

  lazy val viewOverlaidRectangle: Html[Msg] =
    div(id := OverlaidRectangleId, className := "overlaid-shape")()

  lazy val viewOverlaidCircle: Html[Msg] =
    div(id := OverlaidCircleId, className := "overlaid-shape circular-shape")()
