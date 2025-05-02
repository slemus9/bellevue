package bellevue.verified.domain

import stainless.collection.List

final case class Image(
    pixels: List[Int],
    width: Int,
    height: Int
)
