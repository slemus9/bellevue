package bellevue.transformers

import bellevue.dom.DrawingEnvironment
import bellevue.domain.DrawingModel
import bellevue.logic.context.Variation
import bellevue.verified.context as verified
import bellevue.verified.dom.Cmd as VCmd
import bellevue.verified.domain.DrawingModel as VDrawingModel
import io.scalaland.chimney.Transformer

object all
    extends DrawingEnvironment,
      CoreTransformers,
      GeometryTransformers,
      ToolsTransformers,
      DomainTransformers,
      CommandTransformers,
      VariationTransformers:

  given Transformer[verified.Variation[VDrawingModel, VCmd], Variation[DrawingModel, VCmd]] =
    verifiedVariation[VDrawingModel, DrawingModel, VCmd]
