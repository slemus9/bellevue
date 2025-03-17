package bellevue.domain.geometry

import bellevue.domain.ParseError
import cats.syntax.all.*

import scala.scalajs.js.typedarray.Uint8ClampedArray

/**
  * Represents the Image Data taken from an HTML canvas as specified here:
  * https://developer.mozilla.org/en-US/docs/Web/API/ImageData
  */
final class Image private (
    pixels: Uint8ClampedArray,
    width: Int,
    val height: Int
):
  /**
    * @param x
    *   x coordinate of a pixel
    * @param y
    *   y coordinate of a pixel
    * @return
    *   get the pixel at position (x, y). If this is an out of bounds position, it returns None
    */
  def apply(x: Int, y: Int): Option[RGBA] =
    Option.when(
      0 <= x && x < width
        && 0 <= y && y < height
        && (y * width + x) * 4 < pixels.length
        && (y * width + x) * 4 + 3 < pixels.length
    ):
      val pixelOffset = (y * width + x) * 4
      // We can assume this is a Uint8, since it came from a Uint8ClampedArray
      RGBA(
        red = Uint8.assume(pixels(pixelOffset)),
        green = Uint8.assume(pixels(pixelOffset + 1)),
        blue = Uint8.assume(pixels(pixelOffset + 2)),
        alpha = Uint8.assume(pixels(pixelOffset + 3))
      )

end Image

object Image:

  def from(array: Uint8ClampedArray, width: Int, height: Int): Either[ParseError, Image] =
    for
      _ <- Either.raiseWhen(width < 1)(ParseError("Width of an image should be of at least 1 pixel"))
      _ <- Either.raiseWhen(height < 1)(ParseError("Height of an image should be of at least 1 pixel"))
    yield Image(pixels = array, width, height)
