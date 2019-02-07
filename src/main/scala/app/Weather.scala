package app

import cats.effect.Async
import cats.mtl.ApplicativeAsk
import cats.tagless._
import app.Config._
import cats.Id
import mtl.constantAsk

@finalAlg
trait Weather[F[_]] {
  def forecast(city: City): F[Forecast]
}

object Weather {
  def weather[F[_] : Async](config: Config): Weather[F] = new Weather[F] {
    implicit val configAsk = constantAsk[Id, Config](config)
    val client = new WeatherClient(host[Id], port[Id])

    def forecast(city: City): F[Forecast] = Async[F].delay(client.forecast(city))
  }
}

case class Config(host: String, port: Int)

object Config {
  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]

  def host[F[_] : ConfigAsk]: F[String] =
    implicitly[ConfigAsk[F]].reader(_.host)

  def port[F[_] : ConfigAsk]: F[Int] =
    implicitly[ConfigAsk[F]].reader(_.port)
}
