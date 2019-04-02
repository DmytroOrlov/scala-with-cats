package app

case class Temperature(value: Int, unit: TempUnit = Celcius)

case class Forecast(temperature: Temperature) extends AnyVal

case class City(name: String) extends AnyVal
