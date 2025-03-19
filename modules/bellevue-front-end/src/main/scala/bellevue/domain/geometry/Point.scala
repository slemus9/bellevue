package bellevue.domain.geometry

final case class Point(x: Double, y: Double):

  def distanceTo(that: Point): Double =
    val deltax = this.x - that.x
    val deltay = this.y - that.y
    math.sqrt(deltax * deltax + deltay * deltay)
