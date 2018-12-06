package app

import monix.eval.Task

import scala.io.StdIn

trait Console[F[_]] {
  def printLn(line: String): F[Unit]

  def readLn: F[String]
}

object Console {
  def apply[F[_]](implicit consol: Console[F]): Console[F] = consol

  val monixConsole: Console[Task] = new Console[Task] {
    def printLn(line: String): Task[Unit] =
      Task.delay(println(line))

    def readLn: Task[String] = Task.delay(StdIn.readLine)
  }
}
