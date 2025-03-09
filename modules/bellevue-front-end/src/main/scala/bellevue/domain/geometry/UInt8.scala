package bellevue.domain.geometry

import bellevue.domain.ParseError
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.numeric.Interval

/**
  * Maybe there is already an efficient implementation of unsigned ints in scala?
  *
  * TODO: explore https://github.com/typelevel/spire
  */
opaque type Uint8 <: Int = Int :| Uint8Range

type Uint8Range = Interval.Closed[0, 255]

object Uint8 extends RefinedTypeOps[Int, Uint8Range, Uint8]:

  def parse(x: Int): Either[ParseError, Uint8] =
    this.either(x).left.map(ParseError.apply)
