package bellevue.domain.tools

import bellevue.domain.geometry.Pixels
import io.github.iltotore.iron.autoRefine

final case class StyleConfig(
    color: Color,
    lineWidth: Pixels,
    fillStyle: Option[Color]
)

opaque type BrushConfig <: StyleConfig = StyleConfig

object BrushConfig:

  val init: BrushConfig = StyleConfig(
    color = Color("#0047AB"),
    lineWidth = Pixels(2),
    fillStyle = None
  )

  val LineWidths: List[Pixels] =
    List(1, 2, 3, 5, 8, 12, 25, 35, 50, 75, 100).map(Pixels.apply)

final case class EraserConfig(
    style: StyleConfig,
    radius: Pixels
)

object EraserConfig:

  val EraserStyle = StyleConfig(
    color = Color.White,
    lineWidth = Pixels(1),
    fillStyle = Some(Color.White)
  )

  val init = EraserConfig(style = EraserStyle, radius = Pixels(5))

  val EraserRadius: List[Pixels] =
    List(5, 10, 20, 30, 40, 50).map(Pixels.apply)
