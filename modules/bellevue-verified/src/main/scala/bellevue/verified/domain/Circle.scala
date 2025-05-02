package bellevue.verified.domain

import stainless.lang.Real

final case class Circle(center: Point, radius: Real)

object Circle:

  final case class FromCenter(center: Point, to: Point)
