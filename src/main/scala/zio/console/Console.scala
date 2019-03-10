package zio.console

import java.io.IOException

import scalaz.zio.{IO, ZIO}

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
      def printLn(line: String): ZIO[Any, Nothing, Unit] =
        IO.effectTotal(println(line))

      val readLn: ZIO[Any, IOException, String] =
        IO.effect(StdIn.readLine())
          .refineOrDie {
            case e: IOException => e
          }
    }
  }

  object Live extends Live

}
