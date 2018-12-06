package app

case class City(name: String) extends AnyVal

sealed trait Error

case class UnknownCity(city: String) extends Error
