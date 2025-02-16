package bellevue.domain.geometry

final case class Point(x: Pixels, y: Pixels):

  def distanceTo(that: Point): Pixels =
    val deltax = this.x - that.x
    val deltay = this.y - that.y
    math.sqrt(deltax * deltax + deltay * deltay)
