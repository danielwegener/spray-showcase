package com.example

import akka.actor.{ActorLogging, Props, ActorSystem, Actor}
import spray.routing._
import spray.http._
import MediaTypes._
import akka.io.IO
import spray.can.Http
import akka.io.Tcp.ConnectionClosed

object SprayRoutingExample extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("spray-http-example")

  // create and start our service actor
  val routingActor = system.actorOf(Props[MyRoutingActor])

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(routingActor, interface = "localhost", port = 8080)
}

trait MyRoute extends HttpService {
  val myRoute = {
    get {
      complete(StatusCodes.NotFound)
    }
  }
}

class MyRoutingActor extends HttpServiceActor with ActorLogging with MyRoute {
  def receive = runRoute(myRoute)
}

