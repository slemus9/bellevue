package bellevue

import tyrian.*
import tyrian.Html.*
import cats.effect.IO
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("BelleVueApp")
object Main extends TyrianIOApp[Msg, Model]:

  override def router: Location => Msg = Routing.none(Msg.Noop)

  override def init(flags: Map[String, String]): (Model, Cmd[IO, Msg]) =
    Counter.zero -> Cmd.None

  override def update(model: Model): Msg => (Model, Cmd[IO, Msg]) =
    case Msg.Inc  => model.inc -> Cmd.None
    case Msg.Dec  => model.dec -> Cmd.None
    case Msg.Noop => model     -> Cmd.None

  override def view(model: Model): Html[Msg] =
    div(id := "counter")(
      button(onClick(Msg.Dec))("-"),
      div()(model.toString),
      button(onClick(Msg.Inc))("+")
    )

  override def subscriptions(model: Model): Sub[IO, Msg] =
    Sub.None

type Model = Counter

opaque type Counter <: Int = Int

object Counter:
  val zero: Counter = 0

  extension (counter: Counter)
    def inc: Counter = counter + 1
    def dec: Counter = counter - 1

enum Msg:
  case Inc, Dec, Noop
