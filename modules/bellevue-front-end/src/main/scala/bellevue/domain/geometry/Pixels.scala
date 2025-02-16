package bellevue.domain.geometry

import cats.Show
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Positive0

opaque type Pixels <: Int = Int :| Positive0

object Pixels extends RefinedTypeOps[Int, Positive0, Pixels]:

  def parse(str: String): Either[String, Pixels] =
    str match
      case s"$x px"     => fromNumString(x)
      case s"$x pixel"  => fromNumString(x)
      case s"$x pixels" => fromNumString(x)
      case x            => fromNumString(x)

  private def fromNumString(str: String): Either[String, Pixels] =
    str.toIntOption.toRight("A Pixel must be a number").flatMap(this.either)

  given Show[Pixels] = x => s"$x px"
