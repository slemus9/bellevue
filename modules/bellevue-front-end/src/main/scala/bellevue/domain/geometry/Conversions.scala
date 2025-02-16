package bellevue.domain.geometry

object Conversions:

  given Conversion[Double, Pixels] = Pixels.apply

// So that they are available in the geometry package:
export Conversions.given
