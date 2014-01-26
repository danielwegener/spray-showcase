## spray is

* HTTP/REST API
* network abstraction for actors
* HTTP server/client

Notes:
hi

<!-- click -->
## For the Java-EE-dude

Spray is

* a servlet-(spec/container) + HTTPClient + JAX-RS
* but _async_, _non-blocking_, _fast_
* ... on ![img](assets/akka-logo.png)<!-- .element: class="transparent" style="display:inline; margin-bottom: -1em" --> _actors_

<!-- click -->
## What spray is _not_

* MVC framework
* UI-Component Framework
* _better try play/lift_

<!-- click -->
## Actor model

* Component model for concurrent applications
* We are talking about:
    * actors sending messages to other actors mailboxes
    * other actors _react_ and may send further messages

Notes:

Do you know actors? May we skip?
PPL Claim (CITE!): Pure form of OOP (Objects sending messages) with real control flow
Its not like passing your own execution-control to a called object


<!-- click -->
## Actors...

* _react_
* ... can only do one thing at time (logically single-threaded)
* ... can have a __local__ state


<!-- click -->
## Actors are...

* hierarchical (actor-tree)
    * Each actor has a parent
    * Each actor may have children
* alive (have a lifecycle)<!-- .element: class="fragment" -->
    * actors can be spawned at runtime
    * can die due to failures
    * can be revived by their supervising parents
* cheap <!-- .element: class="fragment" -->
    * your JVM can host thousands++ of them


<!-- click -->
### spray modules

![img](assets/spray_components.svg)<!-- .element: class="transparent" style="width: 100%; height:100%" -->


<!-- click -->
![img](assets/messageStack.svg)<!-- .element: class="transparent" style="width: 100%; height:100%" -->

Notes:

* imagine ByteString as immutable byte[]-array

<!-- click -->
### akka-io

* message and actor based abstraction over Java NIO asynchronous channels
* stream-like channel data ⇔ actor friendly events
* formerly known as spray-io (akka-io since 2.2.0)

<!-- click -->
### akka-io

* Acts in server and client role
    * incoming data = Event
    * outgoing data = Command
* domain: payload (ByteStrings) and connection-events
* can be used for any type of protocol
* supports TCP and UDP

Notes:
Wraps nio Socket channels and selectors.
Passes 'events'

<!-- click -->
### akka-io

```scala
package akka.io
object Tcp {
    sealed trait Message

    trait Command extends Message
    case class Connect(remoteAddress, localAddress, options, timeout)
        extends Command
    case class Bind(handler: ActorRef, localAddress, backlog, options)
        extends Command
    case class Write(data: ByteString, ack: Event) extends Command
    case object Close extends Command

    trait Event extends Message
    case class Connected(remoteAddress, localAddress) extends Event
    case class Received(data: ByteString) extends Event
    sealed trait ConnectionClosed extends Event
    ...

```
But: There is no _read_!

Notes:
You see a lot of familiar nouns (remember java Sockets?)

<!-- click -->
How to transform TCP-messages to usable application messages?

akka-io brings a pattern called __pipelines__

pipelines consist of typed PipePairs

you may imagine them like OSI-layers

each layer can only talk to its neighbours


<!-- click -->
![img](assets/pipelineStage.svg)<!-- .element: class="transparent" style="width: 100%; height:100%" -->

```scala
trait PipePair[CmdAbove, CmdBelow, EvtAbove, EvtBelow] {
    def commandPipeline: CmdAbove ⇒ Iterable[Either[EvtAbove, CmdBelow]]
    def eventPipeline: EvtBelow ⇒ Iterable[Either[EvtAbove, CmdBelow]]
}
```

<!-- click -->
### protocol pipelines

![img](assets/pipelineComposition.svg)<!-- .element: class="transparent" style="width: 100%; height:100%" -->

<!-- click -->



<!-- click -->
combining pipes:

```scala
abstract class PipelineStage[CmdAbove, CmdBelow, EvtAbove, EvtBelow] {
...
  def >>[CmdBelowBelow, EvtBelowBelow, BelowContext <: Context]
  (right: PipelineStage[CmdBelow, CmdBelowBelow, EvtBelow, EvtBelowBelow])
  : PipelineStage[CmdAbove, CmdBelowBelow, EvtAbove, EvtBelowBelow] = ...
}
```

<!-- click -->
### spray-can

* Provides akka-io _pipelines_ for TCP ⇔ HTTP
* Abstracts raw ByteString events to
* Supports

<!-- click -->
### and spray-servlet?

* Adapter layer that provides a can on a servlet-container
* Uses Servlet 3 async
* Helpful for soft migration strategies
* Technically a servlet that hosts an actor system


<!-- click -->
### spray-http

* immutable API for the HTTP protocol
* (parboiled: PEG parser)




<!-- click -->

###
    case class HttpMessage()
    case class HttpRequest()
    case class HttpResponse()
    case class HttpHeader()

<!-- click -->

### spray-httpx

* tools for HTTP-handling
* (de)compression
* encoding
* content-negotination

<!-- click -->

### spray-json

* `implicit` based json-serialization
* uses jackson

<!-- click -->

### spray-routing

* Routes
* Directives

<!-- click -->
### Directives are shapeless

```scala
package spray

import shapeless._

package object routing {

  type Route = RequestContext ⇒ Unit
  type RouteGenerator[T] = T ⇒ Route
  type Directive0 = Directive[HNil]
  type Directive1[T] = Directive[T :: HNil]
  type PathMatcher0 = PathMatcher[HNil]
  type PathMatcher1[T] = PathMatcher[T :: HNil]

}
```

<!-- click -->
## spray-client

* sits on top of spray-can and akka-io
* uses an actor system
* => no actors, no client



