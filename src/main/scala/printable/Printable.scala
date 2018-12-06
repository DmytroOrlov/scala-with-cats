package printable

trait Printable[A] {
  def format(vale: A): String
}

object Printable {
  def format[A: Printable](a: A): String = Printable[A].format(a)

  def print[A: Printable](a: A): Unit = println(format(a))

  def apply[A](implicit printable: Printable[A]): Printable[A] = printable
}
