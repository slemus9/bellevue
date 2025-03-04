package bellevue.logic.context

import cats.data.{NonEmptyList, State}
import cats.syntax.all.*
import cats.Monoid

/**
  * Represents a partial [[Behavior]] (state transition) with a (de)activation mechanism. When the variation is active,
  * it may modify the state [[S]] and yield a result of type [[A]].
  *
  * Multiple [[Variation]]s can be composed together to create a new [[Variation]]. Each [[Variation]] can use the
  * result of the previously active [[Variation]] in the composition to define its own [[Behavior]]
  */
trait Variation[S, A] extends BehaviorOps[S, A]:
  self =>

  /**
    * @param state
    *   the current state
    * @return
    *   true if this variation is active, false otherwise
    */
  def isActive(state: S): Boolean

  /**
    * Run this variation with the given initial state and result value
    */
  def runWith(s: S, a: A): (S, A) =
    self.run(a).run(s).value

  /**
    * @return
    *   the state transition of this variation when active
    */
  protected def run: Behavior[S, A]

  /**
    * Compose two variations sequentially, such that the result of the first one is piped into the second one whenever
    * both variations are active. Otherwise, the resulting behavior corresponds to whichever variation is active
    */
  final infix def andThen(that: Variation[S, A]) = new Variation[S, A]:

    override def isActive(state: S): Boolean =
      self.isActive(state) || that.isActive(state)

    override val run: Behavior[S, A] = previous =>
      State.get.flatMap { s =>
        (self.isActive(s), that.isActive(s)) match
          case (true, false) => self.run(previous)
          case (false, true) => that.run(previous)
          case (true, true)  => self.run(previous) >>= that.run
          case _             => passthrough(previous)
      }

  /**
    * Compose two variations such that the resulting behavior is defined by the first active variation. If both
    * variations are active, the resulting behavior corresponds to first one
    */
  final infix def orElse(that: Variation[S, A]) = new Variation[S, A]:

    override def isActive(state: S): Boolean =
      self.isActive(state) || that.isActive(state)

    override val run: Behavior[S, A] = previous =>
      State.get.flatMap { s =>
        (self.isActive(s), that.isActive(s)) match
          case (true, _)     => self.run(previous)
          case (false, true) => that.run(previous)
          case _             => passthrough(previous)
      }

end Variation

object Variation:

  /**
    * Left to right sequential composition of the given [[variations]] sequence. The result of each active variation is
    * piped into the next one in the sequence
    */
  def sequence[S, A](variation: Variation[S, A], variations: Variation[S, A]*): Variation[S, A] =
    sequence(NonEmptyList.of(variation, variations*))

  def sequence[S, A](variations: NonEmptyList[Variation[S, A]]): Variation[S, A] =
    variations.reduceLeft(_ andThen _)

  /**
    * Left to right composition of the given [[variations]] sequence. The behavior of the new variation will correspond
    * to the first active variation that was found in the sequence
    */
  def oneOf[S, A](variation: Variation[S, A], variations: Variation[S, A]*): Variation[S, A] =
    oneOf(NonEmptyList.of(variation, variations*))

  def oneOf[S, A](variations: NonEmptyList[Variation[S, A]]): Variation[S, A] =
    variations.reduceLeft(_ orElse _)

  /**
    * A variation that is able to combine the result of the previously active variation using a monoid instance
    */
  trait Monoidal[S, A: Monoid as monoid] extends Variation[S, A]:

    /**
      * A state transition that does not modify the state, and returns the result of applying the given [[action]] to
      * the current state, while combining it with the previously active variation's result
      */
    protected final def execAndMerge(action: S => A): Behavior[S, A] =
      super.exec(previous => s => previous |+| action(s))

    protected final def partialExecAndMerge(action: PartialFunction[S, A]): Behavior[S, A] =
      execAndMerge(action.lift.andThen(_.getOrElse(monoid.empty)))
