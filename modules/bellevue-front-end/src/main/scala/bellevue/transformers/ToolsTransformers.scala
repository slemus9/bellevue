package bellevue.transformers

import bellevue.domain.tools.*
import bellevue.verified.domain as verified
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait ToolsTransformers:
  self: CoreTransformers =>

  given Iso[verified.Tool, Tool] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )
