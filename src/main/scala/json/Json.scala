package json

sealed trait Json

final case class JsObject(get: Map[String, Json]) extends Json

final case class JsString(get: String) extends Json

final case class JsNumber(get: Double) extends Json

case object JsNull extends Json

object Json {
  def toJson[A: JsonWriter](a: A): Json = JsonWriter[A].write(a)
}

trait JsonWriter[A] {
  def write(value: A): Json
}

object JsonWriter {
  def apply[A](implicit jsonWriter: JsonWriter[A]): JsonWriter[A] = jsonWriter
}
