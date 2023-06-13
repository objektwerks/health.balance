package objektwerks

sealed trait Command

sealed trait License:
  val license: String