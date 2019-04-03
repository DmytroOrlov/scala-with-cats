package zioapp

import app.{City, Config, Requests, Temperature, UnknownCity, WeatherClient}
import cats.implicits._
import scalaz.zio._
import scalaz.zio.console._
import zioapp.config._
import zioapp.weather._

object ZioApp extends App {
  def hottestCity(requests: Ref[Requests]): UIO[(City, Temperature)] =
    for (reqs <- requests.get) yield Requests.hottest(reqs).map(_.temperature)

  def fetchForecast(client: WeatherClient, city: City, requests: Ref[Requests]) =
    for {
      reqs <- requests.get
      maybeForecast = reqs.get(city)
      forecast <- maybeForecast.fold(forecast(client, city))(IO.succeed)
      _ <- requests.update(_ + (city -> forecast))
    } yield forecast

  def cityByName(cityName: String): IO[UnknownCity, City] = {
    def succeed = IO.succeed(City(cityName))

    cityName match {
      case "Wroclaw" ⇒ succeed
      case "Cadiz" ⇒ succeed
      case "Hamburg" ⇒ succeed
      case _ ⇒ IO.fail(UnknownCity(cityName))
    }
  }

  def askCity = putStrLn("What is the next city?") *> getStrLn

  def askFetchJudge(reqs: Ref[Requests], client: WeatherClient) =
    for {
      cityName <- askCity
      city <- cityByName(cityName)
      f <- fetchForecast(client, city, reqs)
      _ <- putStrLn(s"Forecast for $city is ${f.temperature}")
      hottest <- hottestCity(reqs)
      _ <- putStrLn(s"Hottest city found so far is $hottest")
    } yield ()

  def app(requests: Requests) = {
    val makeLoop = for {
      reqs <- Ref.make(requests)
      cl <- weatherClient
      _ <- askFetchJudge(reqs, cl).forever
    } yield ()

    val program = for {
      h <- host
      p <- port
      _ <- putStrLn(s"Using weather service at http://$h:$p")
      _ <- makeLoop
    } yield ()

    program.foldM(
      e ⇒ putStr(s"Encountered an error: $e")
        .const(1),
      _ ⇒ IO
        .succeed(0)
    )
  }

  def run(args: List[String]) = {
    val conf = Config("localhost", 8080)
    val requests = Requests.empty

    app(requests).provideSome[Console](c ⇒
      new Console with Weather.Live with HasConfig {
        val console = c.console

        def apply(v1: Any) = conf
      })
  }
}
