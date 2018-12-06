package app

class WeatherClient(host: String, port: Int) {
  def forcast(city: City): Forcast = city match {
    case City("Wroclaw") => Forcast(Temperature(7))
    case City("Cadiz") => Forcast(Temperature(25))
  }
}
