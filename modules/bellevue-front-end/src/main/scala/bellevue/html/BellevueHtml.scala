package bellevue.html

import bellevue.domain.*
import tyrian.*
import tyrian.Html.*

object BellevueHtml:

  def view(model: DrawingModel): Html[Msg] =
    div(id := "bellevue", className := "bellevue-container")(
      div(className := "bellevue-drawing-container")(
        canvas(id := DrawingModel.CanvasId, className := "bellevue-drawing-canvas")()
      ),
      div(id := "toolbox", className := "bellevue-toolbox-container")(
        tyrian.Html.span("Pick your color: "),
        input(`type` := "color", value := DrawingModel.InitialLineColor, onChange(Msg.PickColor.apply))
      )
    )
