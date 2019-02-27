package zio

import scalaz.zio.Runtime
import scalaz.zio.App
import scalaz.zio.console._
import scalaz.zio.internal.{Platform, PlatformLive}

object MyApp extends App {
  def run(args: List[String]) = app

  val program =
    for {
      _ <- putStrLn("Hello! What is your name?")
      n <- getStrLn
      _ <- putStrLn(s"Hello, ${n}, good to meet you!")
    } yield ()

  val app = program.either.map(_.fold(_ => 1, _ => 0))
/*
  val appLive =
    app.provide(Console.Live)

  sys.exit(new MyRuntime {}.unsafeRun(appLive))

  trait MyRuntime extends Runtime[Console] {
    type Environment = Console

    val Platform: Platform = PlatformLive.Default
    val Environment: Environment = Console.Live
  }
*/
}
