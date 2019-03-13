package app

class WeatherClient(host: String, port: Int) {
  def forecast(city: City): Forecast = city match {
    case City("Wroclaw") ⇒ Forecast(Temperature(7))
    case City("Cadiz") ⇒ Forecast(Temperature(25))
  }
}
