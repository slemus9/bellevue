package bellevue.domain.geometry

final case class Circle(center: Point, radius: Pixels):

  def diameter: Pixels = 2 * radius

object Circle:

  def apply(center: Point, to: Point) =
    new Circle(center, radius = center.distanceTo(to))
