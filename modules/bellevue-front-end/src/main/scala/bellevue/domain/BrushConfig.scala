package bellevue.domain

import bellevue.domain.geometry.Pixels
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Positive0

final case class BrushConfig(
    color: String,
    lineWidth: Pixels
)

object BrushConfig:

  val EraserColor = "#ffffff"

  val init = BrushConfig(
    color = "#0047AB",
    lineWidth = Pixels(2)
  )

  val lineWidths: List[Pixels] =
    List[Double](1, 2, 3, 5, 8, 12, 25, 35, 50, 75, 100).map(Pixels.apply)
