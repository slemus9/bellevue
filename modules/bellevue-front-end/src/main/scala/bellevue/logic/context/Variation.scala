package bellevue.logic.context

import bellevue.logic.context.Variation.Behavior
import cats.data.{NonEmptyList, State}
import cats.syntax.all.*
import cats.Monoid

/**
  * Represents a partial [[Behavior]] (state transition) with a (de)activation mechanism. When the variation is active,
  * it may modify the state [[S]] and yield a result of type [[A]].
  *
  * Multiple [[Variation]]s can be composed together to create a new [[Behavior]]. Each [[Variation]] can use the result
  * of the previously active [[Variation]] in the composition to define its own [[Behavior]]
  */
trait Variation[S, A]:

  /**
    * @param state
    *   the current state
    * @return
    *   true if this variation is active, false otherwise
    */
  def isActive(state: S): Boolean

  /**
    * @return
    *   the state transition of this variation. If it's inactive, it does not modify the state, and it returns the
    *   result of the previously active variation
    */
  def behavior: Behavior[S, A] = previous =>
    State.get.flatMap:
      case s if isActive(s) => run(previous)
      case s                => passthrough(previous)

  /**
    * @return
    *   the state transition of this variation when active
    */
  protected def run: Behavior[S, A]

  /**
    * Auxiliary constructors to define Behaviors
    */

  /**
    * A state transition that modifies the state in accordance to the given [[set]] function, and returns the result of
    * the previously active variation
    */
  protected final def setter(set: S => S): Behavior[S, A] =
    previous => State(s => (set(s), previous))

  /**
    * A state transition that does not modify the state, and returns the result of applying the given [[action]]
    * function to the current state, and the the previously active variation result
    */
  protected final def exec(action: A => S => A): Behavior[S, A] =
    previous => State(s => (s, action(previous)(s)))

  protected final def partialSetter(set: PartialFunction[S, S]): Behavior[S, A] =
    setter(s => set.applyOrElse(s, identity))

  protected final def partialExec(action: A => PartialFunction[S, A]): Behavior[S, A] =
    exec(previous => action(previous).lift.andThen(_.getOrElse(previous)))

  /**
    * A state transition that does not modify the state, and returns the result of the previously active variation
    */
  private def passthrough: Behavior[S, A] =
    setter(identity)

end Variation

object Variation:

  /**
    * Left to right composition of the given [[variations]] to create a new [[Behavior]]
    */
  def compose[S, A](variations: NonEmptyList[Variation[S, A]]): Behavior[S, A] = initial =>
    variations.reduceLeftTo(_.behavior(initial)) { (stateTransition, variation) =>
      stateTransition >>= variation.behavior
    }

  /**
    * A state transition that depends on a previous computation
    */
  type Behavior[S, A] = A => State[S, A]

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

  object Monoidal:

    def merge[S, A: Monoid](variation: Variation[S, A], variations: Variation[S, A]*): State[S, A] =
      merge(NonEmptyList(variation, variations.toList))

    def merge[S, A: Monoid as monoid](variations: NonEmptyList[Variation[S, A]]): State[S, A] =
      compose(variations)(monoid.empty)

  trait AlwaysActive[S]:
    self: Variation[S, ?] =>

    override final def isActive(state: S): Boolean = true

end Variation
