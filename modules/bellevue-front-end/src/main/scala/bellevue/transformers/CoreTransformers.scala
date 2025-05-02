package bellevue.transformers

import bellevue.domain.geometry.*
import bellevue.verified.domain as verified
import io.scalaland.chimney.Iso
import io.scalaland.chimney.Transformer
import stainless.lang.Real

import scala.scalajs.js.typedarray.Uint8ClampedArray

trait CoreTransformers:

  given Iso[Double, Real] = Iso(
    first = x => new Real(BigDecimal(x)),
    second = _.theReal.toDouble
  )

  given Iso[Int, Uint8] = Iso(
    first = Uint8.assume,
    second = identity
  )

  given Iso[verified.Pixels, Pixels] = Iso(
    first = x => Pixels(x.value.theReal.toDouble),
    second = x => verified.Pixels(new Real(BigDecimal(x)))
  )

  given Iso[stainless.collection.List[Int], Uint8ClampedArray] = Iso(
    first = list => Uint8ClampedArray.of(list.toScala*),
    second = arr => stainless.collection.List.fromScala(arr.toList)
  )

  given [A, A1, B, B1](using
      isoA: Iso[A, A1],
      isoB: Iso[B, B1]
  ): Iso[A => B, A1 => B1] = Iso(
    first = f => a1 => isoB.first.transform(f(isoA.second.transform(a1))),
    second = g => a => isoB.second.transform(g(isoA.first.transform(a)))
  )

  given stainlessOption[A]: Iso[stainless.lang.Option[A], Option[A]] = Iso(
    first = {
      case stainless.lang.Some(x) => Some(x)
      case stainless.lang.None()  => None
    },
    second = {
      case Some(x) => stainless.lang.Some(x)
      case None    => stainless.lang.None()
    }
  )

  given stainlessEither[A, B]: Iso[stainless.lang.Either[A, B], Either[A, B]] = Iso(
    first = {
      case stainless.lang.Right(x) => Right(x)
      case stainless.lang.Left(x)  => Left(x)
    },
    second = {
      case Right(x) => stainless.lang.Right(x)
      case Left(x)  => stainless.lang.Left(x)
    }
  )

  given [A, B](using iso: Iso[A, B]): Iso[stainless.lang.Option[A], Option[B]] =
    Iso(
      first = opt => stainlessOption.first.transform(opt.map(iso.first.transform)),
      second = opt => stainlessOption.second.transform(opt.map(iso.second.transform))
    )

  given stainlessEitherIso[A, A1, B, B1](using
      isoA: Iso[A, A1],
      isoB: Iso[B, B1]
  ): Iso[stainless.lang.Either[A, B], Either[A1, B1]] =
    Iso(
      first = either =>
        stainlessEither.first.transform(
          either.map(isoB.first.transform).left.map(isoA.first.transform)
        ),
      second = either =>
        stainlessEither.second.transform(
          either.map(isoB.second.transform).left.map(isoA.second.transform)
        )
    )

end CoreTransformers
