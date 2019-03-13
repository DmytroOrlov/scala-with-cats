package zio

import scalaz.zio.App
import zio.console._

object MyApp extends App {

  def run(args: List[String]) = app.provide(Console.Live)

  val program =
    for {
      _ <- printLn("Hello! What is your name?")
      n <- readLn
      _ <- printLn(s"Hello, ${n}, good to meet you!")
    } yield ()

  val app = program.either.map(_.fold(_ ⇒ 1, _ ⇒ 0))
}
