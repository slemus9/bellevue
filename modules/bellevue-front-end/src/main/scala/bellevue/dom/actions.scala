package bellevue.dom

import bellevue.domain.{HtmlElementIdNotFound, InvalidTypeError}
import cats.syntax.all.*
import org.scalajs.dom

import scala.reflect.{ClassTag, TypeTest}
import scala.scalajs.js

/**
  * Top level definitions of common DOM operations
  */
object actions:

  def getById[A](id: String)(using TypeTest[js.Any, A], ClassTag[A]): A =
    getElementById(id).flatMap(_.as[A]).throwError

  def getElementById(id: String): Either[Throwable, dom.Element] =
    for
      nullableElem <- Either.catchNonFatal(dom.document.getElementById(id))
      element      <- Option(nullableElem).toRight(HtmlElementIdNotFound(id))
    yield element

  extension (value: js.Any)

    def as[A](using TypeTest[js.Any, A], ClassTag[A]): Either[InvalidTypeError, A] =
      value match
        case value: A => value.pure
        case value    => InvalidTypeError.invalidInstance(value).raiseError

    def castTo[A](using TypeTest[js.Any, A], ClassTag[A]): A =
      value.as[A].throwError

  extension [A](either: Either[Throwable, A])
    def throwError: A =
      either match
        case Left(error)  => throw error
        case Right(value) => value
