package bellevue.logic.context

import cats.data.State

/**
  * A state transition that depends on a previous computation
  */
type Behavior[S, A] = A => State[S, A]

/**
  * Auxiliary constructors to define Behaviors
  */
trait BehaviorOps[S, A]:

  /**
    * A state transition that modifies the state in accordance to the given [[set]] function, and returns the result of
    * the previous computation
    */
  protected final def setter(set: S => S): Behavior[S, A] =
    previous => State(s => (set(s), previous))

  /**
    * A state transition that does not modify the state, and returns the result of applying the given [[action]]
    * function to the current state, and the result of the previous computation
    */
  protected final def exec(action: A => S => A): Behavior[S, A] =
    previous => State(s => (s, action(previous)(s)))

  protected final def partialSetter(set: PartialFunction[S, S]): Behavior[S, A] =
    setter(s => set.applyOrElse(s, identity))

  protected final def partialExec(action: A => PartialFunction[S, A]): Behavior[S, A] =
    exec(previous => action(previous).lift.andThen(_.getOrElse(previous)))

  /**
    * A state transition that does not modify the state, and returns the result of the previous computation
    */
  protected def passthrough: Behavior[S, A] =
    setter(identity)
