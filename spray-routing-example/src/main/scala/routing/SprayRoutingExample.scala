package routing

import spray.routing._
import spray.http._
import spray.can._
import akka.actor._
import akka.io.IO
import spray.http.HttpHeaders.`User-Agent`
import spray.http.HttpResponse
import scala.concurrent.duration._
import akka.pattern.ask


import scala.Some

object SprayRoutingExample extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("spray-http-example")

  val orderActor = system.actorOf(Props[MyAnswerActor])
  // create and start our service actor
  val routingActor = system.actorOf(Props[MyRoutingActor](new MyRoutingActor(orderActor)))

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(routingActor, interface = "localhost", port = 8080)
}


sealed case class AnswerRequest(orderNumber:Int)
sealed case class AnswerResponse(result:String)

class MyAnswerActor extends Actor {
  override def receive = {
    case AnswerRequest(_) => sender ! AnswerResponse("42!")
  }
}

class MyRoutingActor(val businessActorRef:ActorRef) extends HttpServiceActor with ActorLogging {

  implicit val timeout = akka.util.Timeout(2.seconds)
  implicit val executionContext = context.system.dispatcher

  def extractUserAgent: HttpHeader ⇒ Option[`User-Agent`] = {
    case h: `User-Agent` ⇒ Some(h)
    case x               ⇒ None
  }

  def receive = runRoute {
    get {
      path("answers" / IntNumber) { number ⇒
        complete((businessActorRef ? AnswerRequest(number)).mapTo[AnswerResponse].map(_.result))
      } ~ pass {
        complete(HttpResponse(StatusCodes.NotFound, "go away!"))
      }
    }
  }
}
