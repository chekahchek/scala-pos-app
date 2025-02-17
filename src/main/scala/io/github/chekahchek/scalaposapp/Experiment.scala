package io.github.chekahchek.scalaposapp

sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json

// Type Class
trait JsonWriter[A] {
  def write(value: A): Json
}


final case class Person(name: String, email: String)

// Type Class Instances defined as a val
object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    def write(value: String): Json = JsString(value)
  }

  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    def write(value: Person): Json =
      JsObject(Map(
        "name" -> JsString(value.name),
        "email" -> JsString(value.email)
      ))
  }
}

// Type Class Interface
object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
    w.write(value)
}

object JsonSyntax {
  implicit class JsonWriterOps[A](value: A) {
    def toJson(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }


import JsonWriterInstances._
import JsonSyntax._
def main(args: Array[String]) : Unit = {
  val testing = Person("Dave", "Dave@gmail.com").toJson
//  Json.toJson(Person("Dave", "dave@example.com"))

}