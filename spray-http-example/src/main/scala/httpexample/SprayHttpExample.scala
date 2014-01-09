package httpexample


import spray.http._
import spray.can._
import akka.actor.{Actor, ActorLogging, Props, ActorSystem}
import akka.io.IO

object SprayHttpExample extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("spray-http-example")

  // create and start our service actor
  val httpActor = system.actorOf(Props[MyHttpActor])

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(httpActor, interface = "localhost", port = 8080)
}


class MyHttpActor extends Actor with ActorLogging {

  def receive = {
    // when a new connection comes in we register ourselves as the connection handler
    case Http.Connected(remoteAddress,localAddress) =>
      log.info(s"connection from $remoteAddress")
      sender ! Http.Register(self)

    case cc:Http.ConnectionClosed =>
      log.info(s"connection closed ${cc.getErrorCause}")


    case r@HttpRequest(method, uri, headers, entity, httpProtocol) =>
      log.info(r.toString)
      sender ! HttpResponse(status = StatusCodes.OK, entity = HttpEntity("Hello spray"))
  }
}


