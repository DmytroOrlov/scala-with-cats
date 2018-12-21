package app

import cats.data.{EitherT, ReaderT, StateT}
import cats.implicits._
import cats.mtl.{ApplicativeAsk, MonadState}
import cats.{Monad, MonadError}
import monix.eval.Task
import monix.execution.Scheduler

object Main {
  type ConfigAsk[F[_]] = ApplicativeAsk[F, Config]

  def host[F[_] : ConfigAsk]: F[String] =
    implicitly[ConfigAsk[F]].reader(_.host)

  def port[F[_] : ConfigAsk]: F[Int] =
    implicitly[ConfigAsk[F]].reader(_.port)

  type ErrorHandler[F[_]] = MonadError[F, Error]

  def cityByName[F[_] : ErrorHandler](cityName: String): F[City] =
    cityName match {
      case "Wroclaw" => City(cityName).pure[F]
      case "Cadiz" => City(cityName).pure[F]
      case _ => implicitly[ErrorHandler[F]].raiseError(UnknownCity(cityName))
    }

  type RequestsState[F[_]] = MonadState[F, Requests]

  def hottestCity[F[_] : RequestsState]: F[(City, Temperature)] = {
    implicitly[RequestsState[F]].inspect(reqs =>
      Requests.hottest(reqs).map(_.temperature)
    )
  }

  def askCity[F[_] : Console : Monad]: F[String] =
    for {
      _ <- Console[F].printLn("What is the next city?")
      cityName <- Console[F].readLn
    } yield cityName

  def fetchForecast[F[_] : Weather : RequestsState : Monad](city: City): F[Forecast] =
    for {
      maybeForecast <- implicitly[RequestsState[F]].inspect(_.get(city))
      forecast <- maybeForecast.fold(Weather[F].forecast(city))(
        _.pure[F]
      )
      _ <- implicitly[RequestsState[F]].modify(_ + (city -> forecast))
    } yield forecast

  def askFetchJudge[F[_] : Console : Weather : RequestsState : ErrorHandler]: F[Unit] =
    for {
      cityName <- askCity[F]
      city <- cityByName[F](cityName)
      forecast <- fetchForecast[F](city)
      _ <- Console[F].printLn(s"Forecast for $city is ${forecast.temperature}")
      hottest <- hottestCity[F]
      _ <- Console[F].printLn(s"Hottest city found so far is $hottest")
    } yield ()

  def program[F[_] : ConfigAsk : Console : Weather : RequestsState : ErrorHandler]: F[Unit] =
    for {
      h <- host[F]
      p <- port[F]
      _ <- Console[F].printLn(s"Using weather service at http://$h:$p")
      _ <- askFetchJudge[F]/*.forever*/
    } yield ()

  def main(args: Array[String]): Unit = {
    val config = Config("localhost", 8080)
    val requests = Requests.empty

    type Effect0[A] = EitherT[Task, Error, A]
    type Effect1[A] = ReaderT[Effect0, Config, A]
    type Effect[A] = StateT[Effect1, Requests, A]

    implicit val configAsk = mtl.constant[Task, Config](config)
    implicit val console = Console.monixConsole
    implicit val weather = Weather.monixWeather(config)

    val app: Effect[Unit] = program[Effect]

    implicit val io = Scheduler.io("io-scheduler")

    (app.run(requests).run(config).value >>= {
      case Left(error) => console.printLn(s"Encountered an error: $error")
      case Right(_) => ().pure[Task]
    }).runSyncUnsafe
  }
}
