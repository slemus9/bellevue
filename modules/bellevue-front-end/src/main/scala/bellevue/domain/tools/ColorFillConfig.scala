package bellevue.domain.tools

import bellevue.domain.geometry.{Image, Pixels, RGB, Uint8}
import io.github.iltotore.iron.autoRefine

final case class ColorFillConfig(
    color: RGB,
    canvasImage: Option[Image]
):

  lazy val style = StyleConfig(
    color,
    lineWidth = Pixels(1),
    fillStyle = Some(color)
  )

object ColorFillConfig:

  val init = ColorFillConfig(
    color = RGB(Uint8(0), Uint8(71), Uint8(171)),
    canvasImage = None
  )
