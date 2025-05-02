package bellevue.verified.domain

final case class Rectangle(topLeft: Point, bottomRight: Point)

object Rectangle:

  final case class FromEdges(from: Point, to: Point)
