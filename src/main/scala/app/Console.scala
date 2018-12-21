package app

import cats.effect.Async

import scala.io.StdIn

trait Console[F[_]] {
  def printLn(line: String): F[Unit]

  def readLn: F[String]
}

object Console {
  def apply[F[_]](implicit console: Console[F]): Console[F] = console

  def console[F[_] : Async]: Console[F] = new Console[F] {
    def printLn(line: String): F[Unit] =
      Async[F].delay(println(line))

    def readLn: F[String] =
      Async[F].delay(StdIn.readLine)
  }
}
