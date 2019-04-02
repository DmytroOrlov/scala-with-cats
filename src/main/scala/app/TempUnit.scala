package app

sealed trait TempUnit

case object Celcius extends TempUnit

case object Fahren extends TempUnit
