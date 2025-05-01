package bellevue.transformers

import bellevue.domain.*
import bellevue.domain.Msg.Partial
import bellevue.verified.domain as verified
import io.scalaland.chimney.syntax.transformInto
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait DomainTransformers:
  self: CoreTransformers & GeometryTransformers & ToolsTransformers =>

  given Iso[verified.AppError, AppError] = Iso(
    first = {
      case verified.InvalidTypeError(message) => InvalidTypeError(message)
      case verified.HtmlElementIdNotFound(id) => HtmlElementIdNotFound(id)
      case verified.ParseError(message)       => ParseError(message)
    },
    second = Transformer.derive
  )

  given Iso[verified.ControlMsg, ControlMsg] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.MouseMsg, MouseMsg] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.ToolboxMsg, ToolboxMsg] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.Msg.Partial, Msg.Partial] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given msgTo: Transformer[verified.Msg, Msg] =
    case verified.Msg.Control(msg) => msg.transformInto
    case verified.Msg.Mouse(msg)   => msg.transformInto
    case verified.Msg.Toolbox(msg) => msg.transformInto
    case msg: verified.Msg.Partial => msg.transformInto

  given msgFrom: Transformer[Msg, verified.Msg] =
    case msg: Partial    => msg.transformInto
    case msg: ControlMsg => verified.Msg.Control(msg.transformInto)
    case msg: MouseMsg   => verified.Msg.Mouse(msg.transformInto)
    case msg: ToolboxMsg => verified.Msg.Toolbox(msg.transformInto)

  given Iso[verified.Msg, Msg] = Iso(msgTo, msgFrom)

  given Iso[verified.MouseDragging, MouseDragging] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.DrawingModel, DrawingModel] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

end DomainTransformers
