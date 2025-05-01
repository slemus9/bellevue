package bellevue.verified.domain

import stainless.lang.Option

final case class StyleConfig(
    color: RGB,
    lineWidth: Pixels,
    fillStyle: Option[RGB]
)

final case class EraserConfig(
    style: StyleConfig,
    radius: Pixels
)

final case class ColorFillConfig(
    color: RGB,
    canvasImage: Option[Image]
)
