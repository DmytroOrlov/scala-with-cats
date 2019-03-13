package zioapp.console

import java.io.IOException

import scalaz.zio.{IO, UIO, ZIO}

import scala.io.StdIn

trait Console {
  def console: Console.Service[Any]
}

object Console extends {

  trait Service[R] {
    def printLn(line: String): ZIO[R, Nothing, Unit]

    val readLn: ZIO[R, IOException, String]
  }

  trait Live extends Console {
    val console = new Service[Any] {
      def printLn(line: String): UIO[Unit] =
        IO.effectTotal(println(line))

      val readLn: IO[IOException, String] =
        IO.effect(StdIn.readLine())
          .refineOrDie {
            case e: IOException ⇒ e
          }
    }
  }

  object Live extends Live

}
