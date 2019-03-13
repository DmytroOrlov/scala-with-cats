package json

object JsonWriterInstances {
  implicit val JsonWriterString: JsonWriter[String] =
    (value: String) ⇒ JsString(value)

  implicit val JsonWriterPerson: JsonWriter[Person] =
    (value: Person) ⇒
      JsObject(Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)
      ))
}
