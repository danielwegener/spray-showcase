package routing

import spray.json.{RootJsonFormat, DefaultJsonProtocol}
import spray.httpx.SprayJsonSupport._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._
import spray.http._

case class Person(name: String, firstName: String, age: Int)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val PersonFormat:RootJsonFormat[Person] = jsonFormat3(Person)
}

object JsonTest extends App {
  import MyJsonProtocol._
  val bob = Person("Bob", "Parr", 32)

  val marshalledBob: Either[Throwable, HttpEntity] = marshal(bob)
  val tryBobAgain: Either[DeserializationError, Person] = marshalledBob.right.get.as[Person]
  val bobAgain = tryBobAgain.right.get
  Console.println(s"case object: $bob")
  Console.println(s"marshalledBob")
  Console.println(bobAgain)

}