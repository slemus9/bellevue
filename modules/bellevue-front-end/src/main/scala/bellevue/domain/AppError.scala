package bellevue.domain

import scala.reflect.ClassTag
import scala.util.control.NoStackTrace

sealed trait AppError extends NoStackTrace

final class HtmlElementIdNotFound(id: String) extends AppError:
  override val getMessage: String = s"Could not find HTML element with ID: $id"

final class ParseError(message: String) extends AppError:
  override def getMessage: String = message

final class InvalidTypeError(message: String) extends AppError:
  override def getMessage: String = message

object InvalidTypeError:

  def invalidInstance[A](received: Any)(using expectedClass: ClassTag[A]) =
    InvalidTypeError(s"Expected an instance of $expectedClass, but got: $received")
