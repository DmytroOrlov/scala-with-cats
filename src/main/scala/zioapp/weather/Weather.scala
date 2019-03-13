package zioapp.weather

import java.io.IOException

import app.{City, Forecast, WeatherClient}
import scalaz.zio.{IO, ZIO}

trait Weather {
  def weather: Weather.Service[Any]
}

object Weather {

  trait Service[R] {
    def forecast(client: WeatherClient, city: City): ZIO[R, IOException, Forecast]
  }

  trait Live extends Weather {
    val weather = new Service[Any] {
      def forecast(client: WeatherClient, city: City): IO[IOException, Forecast] =
        IO.effect(client.forecast(city))
          .refineOrDie {
            case e: IOException ⇒ e
          }
    }
  }

}
