package bellevue.verified.context

type Behavior[S, A] = A => State[S, A]

object Behavior:

  def setter[S, A](set: S => S): Behavior[S, A] =
    previous => State(s => (set(s), previous))

  /**
    * A state transition that does not modify the state, and returns the result of applying the given [[action]]
    * function to the current state, and the result of the previous computation
    */
  def exec[S, A](action: A => S => A): Behavior[S, A] =
    previous => State(s => (s, action(previous)(s)))

  /**
    * A state transition that does not modify the state, and returns the result of the previous computation
    */
  def passthrough[S, A]: Behavior[S, A] =
    setter(identity)
