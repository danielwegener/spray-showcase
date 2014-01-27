import akka.actor.ActorSystem
import akka.io.PipelineStage
import java.util.logging.Logger
import scala.concurrent.Future
import scala.util.{ Failure, Success }
import spray.client.pipelining._
import spray.http.{ StatusCodes, HttpResponse, HttpRequest }

object MyHttpAskingClient extends App {

  val log = Logger.getAnonymousLogger
  implicit val system = ActorSystem("simple-spray-client")
  implicit val executionContext = system.dispatcher

  // implicit 60 seconds timeout
  val pipeline: HttpRequest ⇒ Future[HttpResponse] = spray.client.pipelining.sendReceive

  // similar to ask-pattern: actorRef ? message  
  val result: Future[HttpResponse] = pipeline(Get("http://www.google.com/?q=the+answer"))

  result andThen {
    case Success(HttpResponse(StatusCodes.OK, _, _, _)) ⇒ log.info("yay google is alive")
    case Success(responseWithUnexpectedStatus)          ⇒ log.info(s"atleast a server reply: $responseWithUnexpectedStatus")
    case f: Failure[_]                                  ⇒ log.warning(s"something really went wrong: $f")
  } andThen { case _ ⇒ system.shutdown() }

}
