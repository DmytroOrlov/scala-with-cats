package app

import java.io.IOException

class WeatherClient(host: String, port: Int) {
  val forecast: City ⇒ Forecast = {
    case City("Wroclaw") ⇒ Forecast(Temperature(7))
    case City("Cadiz") ⇒ Forecast(Temperature(25))
    case _ ⇒ throw new IOException("no forecast")
  }
}
