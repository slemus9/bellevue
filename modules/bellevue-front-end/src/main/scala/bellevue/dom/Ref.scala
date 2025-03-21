package bellevue.dom

final class Ref[A](
    private var maybeValue: Option[A],
    private val init: A,
    private val refresher: A => A
):

  def get: A = maybeValue match
    case None        =>
      maybeValue = Some(init)
      init
    case Some(value) =>
      value

  def refresh: A =
    refresher(get)

object Ref:

  def make[A](init: => A)(refresher: A => A): Ref[A] =
    Ref(maybeValue = None, init, refresher)
