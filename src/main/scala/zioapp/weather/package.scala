package zioapp

import java.io.IOException

import app.{City, Forecast, WeatherClient}
import scalaz.zio.ZIO
import zioapp.config._

package object weather extends Weather.Service[Weather] {
  def forecast(client: WeatherClient, city: City): ZIO[Weather, IOException, Forecast] =
    ZIO.accessM(_.weather.forecast(client, city))


  val weatherClient = for {
    h <- host
    p <- port
  } yield new WeatherClient(h, p)
}
