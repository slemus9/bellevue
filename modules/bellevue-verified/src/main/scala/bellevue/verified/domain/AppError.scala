package bellevue.verified.domain

sealed abstract class AppError

final case class HtmlElementIdNotFound(id: String) extends AppError

final case class ParseError(message: String) extends AppError

final case class InvalidTypeError(message: String) extends AppError
