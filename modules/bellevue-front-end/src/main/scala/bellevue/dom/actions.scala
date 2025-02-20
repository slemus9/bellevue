package bellevue.dom

import bellevue.domain.{HtmlElementIdNotFound, InvalidTypeError}
import cats.effect.IO
import cats.syntax.all.*
import org.scalajs.dom

import scala.reflect.{ClassTag, TypeTest}
import scala.scalajs.js

/**
  * Top level definitions of common DOM operations
  */

def getById[A](id: String)(using TypeTest[js.Any, A], ClassTag[A]): IO[A] =
  getElementById(id).flatMap(_.as[A].liftTo[IO])

def getElementById(id: String): IO[dom.Element] =
  IO(dom.document.getElementById(id)).flatMap: nullableElem =>
    Option(nullableElem).liftTo[IO](HtmlElementIdNotFound(id))

extension (value: js.Any)

  def as[A](using TypeTest[js.Any, A], ClassTag[A]): Either[InvalidTypeError, A] =
    value match
      case value: A => value.pure
      case value    => InvalidTypeError.invalidInstance(value).raiseError
