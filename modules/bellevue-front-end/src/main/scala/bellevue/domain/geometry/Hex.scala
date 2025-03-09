package bellevue.domain.geometry

import bellevue.domain.ParseError

import scala.util.Try

/**
  * Helper object to parse hexadecimal strings
  */
object Hex:

  def parse(str: String): Either[ParseError, Int] =
    Try(Integer.parseInt(str, 16)).toOption.toRight(
      ParseError(s"Not a valid Hexadecimal string: $str")
    )
