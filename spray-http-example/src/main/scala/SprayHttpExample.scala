package com.example

import akka.actor.{ActorLogging, Props, ActorSystem, Actor}
import spray.routing._
import spray.http._
import MediaTypes._
import akka.io.IO
import spray.can.Http
import akka.io.Tcp.ConnectionClosed

object SprayHttpExample extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("spray-http-example")

  // create and start our service actor
  val httpActor = system.actorOf(Props[MyRoutingActor])

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


