package bellevue.verified.domain

// type Msg = ControlMsg | MouseMsg | ToolboxMsg

sealed abstract class Msg

object Msg:
  final case class Control(message: ControlMsg) extends Msg
  final case class Mouse(message: MouseMsg)     extends Msg
  final case class Toolbox(message: ToolboxMsg) extends Msg

sealed abstract class ControlMsg

object ControlMsg:
  final case class HtmlElementLoaded(id: String)                            extends ControlMsg
  final case class MapToCanvas(point: Point, toMouseMsg: Point => MouseMsg) extends ControlMsg
  case object NoAction                                                      extends ControlMsg
  case object ResizeCanvas                                                  extends ControlMsg

sealed abstract class MouseMsg

object MouseMsg:
  final case class MouseDown(point: Point) extends MouseMsg
  final case class MouseMove(point: Point) extends MouseMsg
  final case class MouseUp(point: Point)   extends MouseMsg

sealed abstract class ToolboxMsg

object ToolboxMsg:
  final case class PickBrushSize(size: BigInt)      extends ToolboxMsg
  final case class PickColor(color: RGB)            extends ToolboxMsg
  final case class PickEraserRadius(radius: BigInt) extends ToolboxMsg
  final case class PickFillColor(color: RGB)        extends ToolboxMsg
  final case class PickTool(tool: Tool)             extends ToolboxMsg
  final case class SetCanvasImage(image: Image)     extends ToolboxMsg
