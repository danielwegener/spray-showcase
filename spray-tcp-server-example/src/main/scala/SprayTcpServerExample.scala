import akka.actor.{ ActorSystem, Props, Actor, ActorLogging }

import akka.util.ByteString
import java.net.InetSocketAddress
import akka.io._
import akka.io.Tcp._

object SprayTcpServerExample extends App {

  // we need an ActorSystem to host our application in
  val system = ActorSystem("spray-tcp-server-example")
  // create and start our server actor
  val serverActor = system.actorOf(Props[ServerActor])
  IO(Tcp) ! Bind(serverActor, new InetSocketAddress("127.0.0.1", 1234))
  // ~$ telnet localhost 1234
}

class ServerActor extends Actor with ActorLogging {

  def receive = {
    case Bound(localAddress) ⇒ log.info(s"Now listening on $localAddress")
    case Connected(remoteAddress, localAddress) ⇒
      val handler = context.actorOf(Props[EchoConnectionActor])
      log.info(s"Accepting connection from $remoteAddress)")
      sender ! Register(handler)
  }
}

class EchoConnectionActor extends Actor with ActorLogging {

  def receive = {
    case Received(data) ⇒
      log.info(s"received ${data.size} bytes")
      sender ! Write(data)
    case c: ConnectionClosed ⇒
      log.info(s"connection closed, self stopping: ${c.getErrorCause}")
      context stop self
  }
}