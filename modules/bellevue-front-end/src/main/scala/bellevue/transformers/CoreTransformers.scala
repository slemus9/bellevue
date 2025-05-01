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
