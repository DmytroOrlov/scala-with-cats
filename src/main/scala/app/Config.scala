package app

import cats.mtl.ApplicativeAsk

case class Config(host: String, port: Int)

object Config {
  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]

  def host[F[_] : ConfigAsk]: F[String] =
    implicitly[ConfigAsk[F]].reader(_.host)

  def port[F[_] : ConfigAsk]: F[Int] =
    implicitly[ConfigAsk[F]].reader(_.port)
}
