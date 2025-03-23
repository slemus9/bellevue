package bellevue.verified.context

final class State[S, A](val run: S => (S, A)) extends AnyVal:

  def map[B](f: A => B): State[S, B] =
    State { s0 =>
      val (s1, a) = this.run(s0)
      (s1, f(a))
    }

  def flatMap[B](f: A => State[S, B]): State[S, B] =
    State { s0 =>
      val (s1, a) = this.run(s0)
      f(a).run(s1)
    }

object State:

  def get[S]: State[S, S] =
    State(s => (s, s))
