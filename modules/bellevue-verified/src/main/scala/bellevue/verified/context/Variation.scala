package bellevue.verified.context

import stainless.annotation.law
import stainless.lang.*
import stainless.lang.StaticChecks.*

abstract class Variation[S, A]:

  def isActive(state: S): Boolean

  def run(previous: A, state: S): (S, A) =
    require(this.isActive(state))
    (??? : (S, A))

object Variation:

  abstract class Chainable[S, A]:

    def v1: Variation[S, A]
    def v2: Variation[S, A]

    @law
    def isChainable(previous: A, state: S): Boolean =
      v1.isActive(state) ==> {
        val (newState, _) = v1.run(previous, state)
        v2.isActive(newState)
      }

  final class V1 extends Variation[Option[BigInt], String]:

    override def isActive(state: Option[BigInt]): Boolean =
      state match
        case Some(x) => x > 10
        case _       => false

    override def run(previous: String, state: Option[BigInt]): (Option[BigInt], String) =
      require(this.isActive(state))
      (state.map(_ - 9), "V1")

  final class V2 extends Variation[Option[BigInt], String]:
    override def isActive(state: Option[BigInt]): Boolean =
      state match
        case Some(x) => x > 1
        case _       => false

    override def run(previous: String, state: Option[BigInt]): (Option[BigInt], String) =
      require(this.isActive(state))
      (state.map(_ + 1), "V2")

  val chain = new Chainable[Option[BigInt], String]:
    override def v1: Variation[Option[BigInt], String] = new V1
    override def v2: Variation[Option[BigInt], String] = new V2

end Variation
