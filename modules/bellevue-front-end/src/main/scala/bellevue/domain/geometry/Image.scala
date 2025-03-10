package bellevue.domain.geometry

import bellevue.domain.ParseError
import cats.syntax.all.*

import scala.scalajs.js.typedarray.Uint8ClampedArray

/**
  * Represents the Image Data taken from an HTML canvas as specified here:
  * https://developer.mozilla.org/en-US/docs/Web/API/ImageData
  */
final class Image private (
    pixels: Array[RGBA],
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
    get(x, y)

  /**
    * @param x
    *   x coordinate of a pixel
    * @param y
    *   y coordinate of a pixel
    * @return
    *   a list of neighbors of the pixel located at (x, y). It should contain those pixels who are 1 position up, 1
    *   position down, 1 position to the right and 1 position to the left of the given pixel. Any out-of-bounds
    *   positions ignored
    */
  def neighbors(x: Int, y: Int): List[RGBA] =
    List(
      (x, y - 1),
      (x, y + 1),
      (x + 1, y),
      (x - 1, y)
    ).mapFilter(get)

  /**
    * @param x
    *   x coordinate of a pixel
    * @param y
    *   y coordinate of a pixel
    * @return
    *   get the pixel at position (x, y). If this is an out-of-bounds position, it returns None
    */
  def get(x: Int, y: Int): Option[RGBA] =
    Option.when(0 <= x && x < width && 0 <= y && y < height):
      pixels(y * width + x)

end Image

object Image:

  def from(array: Uint8ClampedArray, width: Int, height: Int): Either[ParseError, Image] =
    for
      _      <- Either.raiseWhen(width < 1)(ParseError("Width of an image should be of at least 1 pixel"))
      _      <- Either.raiseWhen(height < 1)(ParseError("Height of an image should be of at least 1 pixel"))
      pixels <- makePixels(array)
    yield Image(pixels, width, height)

  private def makePixels(array: Uint8ClampedArray): Either[ParseError, Array[RGBA]] =
    if array.size % 4 == 0 then
      println(s"Image size: ${array.size}")
      array
        .grouped(4)
        .map { pixelData =>
          // We can assume this is a Uint8, since it came from a Uint8ClampedArray
          val List(red, green, blue, alpha) = pixelData.toList.map(Uint8.assume)
          RGBA(red, green, blue, alpha)
        }
        .toArray
        .pure
    else ParseError("Expected a 1-dimensional array that stores each RGBA pixel in 4 contiguous positions").raiseError
