package app

import cats.data.{EitherT, StateT}
import cats.implicits._
import cats.mtl.MonadState
import cats.{Monad, MonadError}
import monix.eval.Task
import monix.execution.Scheduler
import mtl._
import cats.mtl.implicits._
import app.Config._

object Main {
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
      _ <- askFetchJudge[F].foreverM[Unit]
    } yield ()

  def main(args: Array[String]): Unit = {
    val config = Config("localhost", 8080)
    val requests = Requests.empty

    type Effect0[A] = EitherT[Task, Error, A]
    type Effect[A] = StateT[Effect0, Requests, A]

    implicit val configAsk = constantAsk[Effect, Config](config)
    implicit val consoleEffect = Console.console[Effect]
    implicit val weather = Weather.weather[Effect](config)

    val app: Effect[Unit] = program[Effect]

    implicit val io = Scheduler.io("io-scheduler")

    (app.run(requests).value >>= {
      case Left(error) =>
        Console.console[Task].printLn(s"Encountered an error: $error")
      case Right(_) =>
        ().pure[Task]
    }).runSyncUnsafe()
  }
}
