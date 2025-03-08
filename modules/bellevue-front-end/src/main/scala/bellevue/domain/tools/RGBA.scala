package bellevue.domain.tools

import bellevue.domain.ParseError
import cats.syntax.all.*
import io.github.iltotore.iron.autoRefine

/**
  * Represents an RGB color with an alpha component for opacity
  */
final case class RGBA(red: Uint8, green: Uint8, blue: Uint8, alpha: Uint8):

  def toRGB = RGB(red, green, blue)

object RGBA:

  def parse(red: Int, green: Int, blue: Int, alpha: Int): Either[ParseError, RGBA] =
    (
      Uint8.parse(red),
      Uint8.parse(green),
      Uint8.parse(blue),
      Uint8.parse(alpha)
    ).mapN(RGBA.apply)

/**
  * Represents color using the RGB model
  */
final case class RGB(red: Uint8, green: Uint8, blue: Uint8):

  def toRGBA = RGBA(red, green, blue, alpha = Uint8(255))

  def toHexString = f"#$red%02X$green%02X$blue%02X"

object RGB:

  val White = RGB(Uint8(255), Uint8(255), Uint8(255))

  private val hexPattern = "^#([0-9a-fA-F]{2})([0-9a-fA-F]{2})([0-9a-fA-F]{2})$".r

  def parse(str: String): Either[ParseError, RGB] =
    str match
      case hexPattern(red, green, blue) =>
        (
          parseComponent(red),
          parseComponent(green),
          parseComponent(blue)
        ).mapN(RGB.apply)

      case str =>
        ParseError(
          s"Invalid RGB Hex string. It should follow the pattern ${hexPattern}, but received: $str"
        ).raiseError

  private def parseComponent(str: String): Either[ParseError, Uint8] =
    Hex.parse(str).flatMap(Uint8.parse)
