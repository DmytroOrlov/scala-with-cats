package app

import monix.eval.Task

trait Weather[F[_]] {
  def forcast(city: City): F[Forcast]
}

object Weather {
  def apply[F[_]](implicit weather: Weather[F]): Weather[F] = weather

  def monixWeather(config: Config): Weather[Task] =
    new Weather[Task] {
      val client = new WeatherClient(config.host, config.port)

      def forcast(city: City): Task[Forcast] =
        Task.delay {
          client.forcast(city)
        }
    }
}

case class Config(host: String, port: Int)
