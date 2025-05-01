package bellevue.transformers

import bellevue.domain.tools.*
import bellevue.verified.domain as verified
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait ToolsTransformers:
  self: CoreTransformers & GeometryTransformers =>

  given Iso[verified.Tool, Tool] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.StyleConfig, StyleConfig] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )

  given Iso[verified.EraserConfig, EraserConfig] = Iso(
    first = Transformer.derive,
    second = Transformer.derive
  )
