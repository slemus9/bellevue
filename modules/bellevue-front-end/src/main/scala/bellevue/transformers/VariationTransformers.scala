package bellevue.transformers

import bellevue.logic.context.Behavior
import bellevue.logic.context.Variation
import bellevue.verified.context as verified
import cats.data.State
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer

trait VariationTransformers:

  given verifiedVariation[S, S1, A](using
      isoS: Iso[S, S1]
  ): Transformer[verified.Variation[S, A], Variation[S1, A]] = variation =>
    new Variation[S1, A]:

      override def isActive(state: S1): Boolean =
        variation.isActive(isoS.second.transform(state))

      override protected def run: Behavior[S1, A] = previous =>
        State { state =>
          val (s, a) = variation.run(previous, isoS.second.transform(state))
          (isoS.first.transform(s), a)
        }

  extension [S, A](v1: verified.Variation[S, A])
    def andThen(v2: verified.Variation[S, A]) = new verified.Variation[S, A]:

      override def isActive(state: S): Boolean =
        v1.isActive(state) || v2.isActive(state)

      override def run(previous: A, state: S): (S, A) =
        (v1.isActive(state), v2.isActive(state)) match
          case (true, false) => v1.run(previous, state)
          case (false, true) => v2.run(previous, state)
          case (true, true)  =>
            val (newState, newPrev) = v1.run(previous, state)
            v2.run(newPrev, newState)
          case _             => (state, previous)

    def orElse(v2: verified.Variation[S, A]) = new verified.Variation[S, A]:

      override def isActive(state: S): Boolean =
        v1.isActive(state) || v2.isActive(state)

      override def run(previous: A, state: S): (S, A) =
        (v1.isActive(state), v2.isActive(state)) match
          case (true, _)     => v1.run(previous, state)
          case (false, true) => v2.run(previous, state)
          case _             => (state, previous)

end VariationTransformers
