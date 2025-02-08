package bellevue.html

import bellevue.domain.*
import tyrian.Html
import tyrian.Html.*

object BellevueHtml:

  val CanvasId = "drawing-canvas"

  def view(model: DrawingModel): Html[Msg] =
    div(id := "bellevue", className := "bellevue-container")(
      div(className := "drawing-container")(
        canvas(id := CanvasId, className := "drawing-canvas")()
      ),
      ToolboxHtml(model).view
    )
