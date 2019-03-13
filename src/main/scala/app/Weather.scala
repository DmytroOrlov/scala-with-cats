package app

import app.Config._
import cats.Id
import cats.effect.Sync
import cats.tagless._
import mtl.constantAsk

@finalAlg
trait Weather[F[_]] {
  def forecast(city: City): F[Forecast]
}

object Weather {
  def weather[F[_] : Sync](config: Config): Weather[F] = new Weather[F] {
    val client = {
      implicit val configAsk = constantAsk[Id, Config](config)
      new WeatherClient(host[Id], port[Id])
    }

    def forecast(city: City): F[Forecast] = Sync[F].delay(client.forecast(city))
  }
}
