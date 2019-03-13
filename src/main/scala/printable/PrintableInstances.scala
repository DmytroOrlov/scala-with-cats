package printable

object PrintableInstances {
  implicit val PrintableString: Printable[String] =
    (s: String) ⇒ s

  implicit val PrintableInt: Printable[Int] =
    (v: Int) ⇒ v.toString
}
