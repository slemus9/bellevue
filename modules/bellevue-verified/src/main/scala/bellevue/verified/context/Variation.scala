package bellevue.verified.context

import bellevue.verified.domain.{DrawingModel, MouseMsg, Msg, Tool}
import bellevue.verified.domain.MouseMsg.MouseDown
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
        case None()  => false

    override def run(previous: String, state: Option[BigInt]): (Option[BigInt], String) =
      require(this.isActive(state))
      state match
        case None()  => (Some(BigInt("1")), "V3")
        case Some(x) => (Some(x + 1), "V2")

  final case class Cmd(
      overlaidRectangleVisible: Boolean
  )

  final class V3 extends Variation[DrawingModel, Option[Cmd]]:
    override def isActive(state: DrawingModel): Boolean =
      state.selectedTool == Tool.Rectangle &&
        state.receivedMessage.isInstanceOf[Msg.Mouse] &&
        state.mouseDragging.isDefined

    override def run(previous: Option[Cmd], state: DrawingModel): (DrawingModel, Option[Cmd]) =
      require(this.isActive(state))
      (state.receivedMessage, state.mouseDragging) match
        case (Msg.Mouse(MouseMsg.MouseDown(to)), None()) =>
          (state, Some(Cmd(true)))

        case _ => (state, None())

  // def wrongState(
  //     prev: Option[Cmd],
  //     mouseDown: MouseMsg.MouseDown,
  //     model: DrawingModel
  // ): Boolean =
  //   val action      = new V3
  //   val inputModel  = model.copy(
  //     selectedTool = Tool.Rectangle,
  //     receivedMessage = Msg.Mouse(mouseDown),
  //     mouseDragging = None()
  //   )
  //   val (_, result) = action.run(prev, inputModel)
  //   result.exists(_.overlaidRectangleVisible)

  val chain = new Chainable[Option[BigInt], String]:
    override def v1: Variation[Option[BigInt], String] = new V1
    override def v2: Variation[Option[BigInt], String] = new V2

end Variation
