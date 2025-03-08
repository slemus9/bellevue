package bellevue.domain.geometry

import bellevue.domain.ParseError
import cats.Show

opaque type Pixels <: Double = Double

object Pixels:

  def apply(x: Int): Pixels = x

  def apply(x: Double): Pixels = x

  def parse(str: String): Either[ParseError, Pixels] =
    str match
      case s"${x}px" => fromNumString(x)
      case x         => fromNumString(x)

  private def fromNumString(str: String): Either[ParseError, Pixels] =
    str.toDoubleOption.toRight(ParseError("A Pixel must be a number"))

  extension (x: Double) def px: Pixels = apply(x)

  given Show[Pixels] = x => s"${x}px"
