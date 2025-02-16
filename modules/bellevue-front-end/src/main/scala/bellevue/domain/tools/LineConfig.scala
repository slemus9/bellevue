package bellevue.domain.tools

import bellevue.domain.geometry.Pixels
import io.github.iltotore.iron.autoRefine

final case class LineConfig(
    color: Color,
    lineWidth: Pixels
)

object LineConfig:

  val init = LineConfig(
    color = Color("#0047AB"),
    lineWidth = Pixels(2)
  )

  val LineWidths: List[Pixels] =
    List[Double](1, 2, 3, 5, 8, 12, 25, 35, 50, 75, 100).map(Pixels.apply)
