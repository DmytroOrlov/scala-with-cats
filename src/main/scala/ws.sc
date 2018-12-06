import json.JsonSyntax._
import json.JsonWriterInstances._
import json.{Json, Person}

val p = Person("Anna", "a.smile@gmail.com")

Json.toJson(p)
p.toJson
"aaa".toJson
