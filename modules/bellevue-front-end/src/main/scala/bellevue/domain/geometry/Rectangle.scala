package bellevue.domain.geometry

/**
  * @param topLeft
  *   the top left coordinate of the rectangle
  * @param bottomRight
  *   the bottom right coordinate of the rectangle
  */
final case class Rectangle(topLeft: Point, bottomRight: Point):

  def width: Double  = bottomRight.x - topLeft.x
  def height: Double = bottomRight.y - topLeft.y

object Rectangle:

  def apply(from: Point, to: Point): Rectangle = (from, to) match
    case (Point(x1, y1), Point(x2, y2)) if y1 < y2 && x1 < x2 => new Rectangle(from, to)
    case (Point(x1, y1), Point(x2, y2)) if y1 < y2            => new Rectangle(Point(x2, y1), Point(x1, y2))
    case (Point(x1, y1), Point(x2, y2)) if x1 < x2            => new Rectangle(Point(x1, y2), Point(x2, y1))
    case (Point(x1, y1), Point(x2, y2))                       => new Rectangle(to, from)
