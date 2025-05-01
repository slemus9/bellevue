package bellevue.transformers

import bellevue.domain.geometry.*
import bellevue.verified.domain as verified
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait GeometryTransformers:
  self: CoreTransformers =>

  given Iso[verified.Circle, Circle] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.Rectangle, Rectangle] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.Image, Image] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.Point, Point] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.RGB, RGB] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.RGBA, RGBA] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )
