package zioapp

import java.io.IOException

import scalaz.zio.ZIO

package object console extends Console.Service[Console] {
  def printLn(line: String): ZIO[Console, Nothing, Unit] =
    ZIO.accessM(_.console printLn line)

  val readLn: ZIO[Console, IOException, String] =
    ZIO.accessM(_.console.readLn)
}
