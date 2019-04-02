package app

import cats.effect.Sync
import cats.tagless._

@finalAlg
trait Weather[F[_]] {
  def forecast(city: City): F[Forecast]
}

object Weather {
  def weather[F[_] : Sync](config: Config): Weather[F] = new Weather[F] {
    lazy val client = new WeatherClient(config.host, config.port)

    def forecast(city: City): F[Forecast] = Sync[F].delay(client.forecast(city))
  }
}
