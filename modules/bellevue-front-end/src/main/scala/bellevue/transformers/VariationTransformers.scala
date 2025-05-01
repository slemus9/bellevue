package bellevue.transformers

import bellevue.logic.context.Behavior
import bellevue.logic.context.Variation
import bellevue.verified.context as verified
import cats.data.State

object VariationTransformers:

  def fromVerified[S, S1, A, A1](
      variation: verified.Variation[S, A],
      transformStateCo: S => S1,
      transformStateContra: S1 => S,
      transformInputCo: A => A1,
      transformInputContra: A1 => A
  ): Variation[S1, A1] = new Variation[S1, A1]:

    override def isActive(state: S1): Boolean =
      variation.isActive(transformStateContra(state))

    override protected def run: Behavior[S1, A1] = previous =>
      State { state =>
        val (s, a) = variation.run(transformInputContra(previous), transformStateContra(state))
        (transformStateCo(s), transformInputCo(a))
      }
