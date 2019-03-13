package zioapp

import app.Config
import scalaz.zio.ZIO

package object config {
  type HasConfig = Any ⇒ Config

  val host = ZIO.access { c: HasConfig ⇒ c(()).host }
  val port = ZIO.access { c: HasConfig ⇒ c(()).port }
}
