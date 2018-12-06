package json

object JsonSyntax {

  implicit class JsonWriterOps[A](val a: A) extends AnyVal {
    def toJson(implicit A: JsonWriter[A]): Json =
      A.write(a)
  }

}
