package bellevue.transformers

import bellevue.domain.geometry.*
import bellevue.verified.domain as verified
import io.scalaland.chimney.dsl.transformInto
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait GeometryTransformers:
  self: CoreTransformers =>

  given Iso[verified.Circle, Circle] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.Circle.FromCenter, Circle] = Iso(
    first = circle => Circle(circle.center.transformInto[Point], circle.to.transformInto[Point]),
    second = circle =>
      verified.Circle.FromCenter(
        circle.center.transformInto[verified.Point],
        to = Point(circle.center.x, circle.center.y + circle.radius).transformInto[verified.Point]
      )
  )

  given Iso[verified.Rectangle.FromEdges, Rectangle] = Iso(
    first = rect => Rectangle(rect.from.transformInto[Point], rect.to.transformInto[Point]),
    second = rect =>
      verified.Rectangle.FromEdges(
        rect.topLeft.transformInto[verified.Point],
        rect.bottomRight.transformInto[verified.Point]
      )
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
end GeometryTransformers
