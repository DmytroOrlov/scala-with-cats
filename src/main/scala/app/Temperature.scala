package app

sealed trait TempUnit

case object Celcius extends TempUnit

case object Fahren extends TempUnit

case class Temperature(value: Int, unit: TempUnit = Celcius)

case class Forecast(temperature: Temperature) extends AnyVal
