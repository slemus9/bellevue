package bellevue.domain.tools

import bellevue.domain.ParseError
import io.github.iltotore.iron.*
import io.github.iltotore.iron.constraint.string.Match

opaque type Color <: String = String :| Match[ColorPattern]

type ColorPattern = "^#(?:[0-9a-fA-F]){6}$"

object Color extends RefinedTypeOps[String, Match[ColorPattern], Color]:

  val White = Color("#ffffff")

  def parse(str: String): Either[ParseError, Color] =
    this.either(str).left.map(ParseError.apply)
