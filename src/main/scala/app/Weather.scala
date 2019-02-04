package app

import cats.effect.Async
import cats.tagless._

@finalAlg
trait Weather[F[_]] {
  def forecast(city: City): F[Forecast]
}

object Weather {
  def weather[F[_] : Async](config: Config): Weather[F] = new Weather[F] {
    val client = new WeatherClient(config.host, config.port)

    def forecast(city: City): F[Forecast] = Async[F].delay(client.forecast(city))
  }
}

case class Config(host: String, port: Int)
