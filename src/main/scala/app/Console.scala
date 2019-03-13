package app

import cats.effect.Sync
import cats.tagless._

import scala.io.StdIn

@finalAlg
trait Console[F[_]] {
  def printLn(line: String): F[Unit]

  def readLn: F[String]
}

object Console {
  def console[F[_] : Sync]: Console[F] = new Console[F] {
    def printLn(line: String): F[Unit] =
      Sync[F].delay(println(line))

    def readLn: F[String] =
      Sync[F].delay(StdIn.readLine)
  }
}
