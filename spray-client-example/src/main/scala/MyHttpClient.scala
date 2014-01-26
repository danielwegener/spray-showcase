import akka.actor._

import scala.concurrent.Future
import scala.util.Success
import spray.client.pipelining._
import akka.io.IO
import spray.can.Http
import scala.concurrent.duration._
import spray.http.{ StatusCodes, HttpResponse, HttpRequest }
import spray.util._

object SprayHttpClientExample extends App {

  implicit val system = ActorSystem("simple-spray-client")
  val myTestClientActorRef = system.actorOf(Props[MyTestClientActor])
  myTestClientActorRef ! CheckGoogleIsAlive

}

object CheckGoogleIsAlive

class MyTestClientActor extends Actor with ActorLogging {

  implicit val executionContext = context.dispatcher
  val timeout = 5.seconds

  val pipeline: HttpRequest ⇒ Future[HttpResponse] = sendReceive(IO(Http)(actorSystem))(executionContext, timeout)

  override def receive: Actor.Receive = {
    case CheckGoogleIsAlive ⇒
      val response: Future[HttpResponse] = pipeline(Get("https://www.google.com/?q=The+Answer"))
      response.onComplete {
        case Success(HttpResponse(StatusCodes.OK, _, _, _)) ⇒ self ! PoisonPill
        case a ⇒ log.info(s"Bad things happened: $a")
      }

  }

}