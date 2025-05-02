package bellevue.verified.context

import stainless.annotation.law
import stainless.lang.*
import stainless.lang.BooleanDecorations
import stainless.lang.StaticChecks.*

abstract class Variation[S, A]:

  def isActive(state: S): Boolean

  def run(previous: A, state: S): (S, A) =
    require(this.isActive(state))
    (??? : (S, A))

object Variation:

  /**
    * Type that verifies a dependency relation that we care about in this application. This relation states that,
    * whenever the first function is active and is applied to any previous: A and state: S values, the second function
    * should be active with respect to the new state
    */
  abstract class Chainable[S, A]:

    def v1: Variation[S, A]
    def v2: Variation[S, A]

    @law
    def isChainable(previous: A, state: S): Boolean =
      v1.isActive(state) ==> {
        val (newState, _) = v1.run(previous, state)
        v2.isActive(newState)
      }

/**
  * Examples of valid Chainable instances
  */
object Example:

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
        case None()  => false

    override def run(previous: String, state: Option[BigInt]): (Option[BigInt], String) =
      require(this.isActive(state))
      state match
        case None()  => (Some(BigInt("1")), "V3")
        case Some(x) => (Some(x + 1), "V2")

  val chain = new Variation.Chainable[Option[BigInt], String]:
    override def v1: Variation[Option[BigInt], String] = new V1
    override def v2: Variation[Option[BigInt], String] = new V2
