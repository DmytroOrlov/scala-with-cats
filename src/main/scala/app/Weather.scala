package app

import monix.eval.Task

trait Weather[F[_]] {
  def forecast(city: City): F[Forecast]
}

object Weather {
  def apply[F[_]](implicit weather: Weather[F]): Weather[F] = weather

  def monixWeather(config: Config): Weather[Task] =
    new Weather[Task] {
      val client = new WeatherClient(config.host, config.port)

      def forecast(city: City): Task[Forecast] =
        Task.delay {
          client.forecast(city)
        }
    }
}

case class Config(host: String, port: Int)
