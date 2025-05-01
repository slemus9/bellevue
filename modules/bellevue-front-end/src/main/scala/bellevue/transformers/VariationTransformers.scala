package bellevue.transformers

import bellevue.logic.context.Behavior
import bellevue.logic.context.Variation
import bellevue.verified.context as verified
import cats.data.State
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait VariationTransformers:

  given [S, S1, A, A1](using
      isoS: Iso[S, S1],
      isoA: Iso[A, A1]
  ): Transformer[verified.Variation[S, A], Variation[S1, A1]] = variation =>
    new Variation[S1, A1]:

      override def isActive(state: S1): Boolean =
        variation.isActive(isoS.second.transform(state))

      override protected def run: Behavior[S1, A1] = previous =>
        State { state =>
          val (s, a) = variation.run(isoA.second.transform(previous), isoS.second.transform(state))
          (isoS.first.transform(s), isoA.first.transform(a))
        }
