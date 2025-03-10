package bellevue.logic

import bellevue.domain.geometry.Point

object ChartAction extends DrawingEnvironment:

  var drawed = false

  def draw(origin: Point) = canvas.run: canvas =>
    canvas.drawChart(origin)
