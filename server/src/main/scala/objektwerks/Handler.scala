package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.typesafe.scalalogging.LazyLogging

import io.helidon.webserver.http.{Handler => WebHandler, ServerRequest, ServerResponse}

import Serializer.given

final class Handler(dispatcher: Dispatcher) extends WebHandler with LazyLogging:
  override def handle(request: ServerRequest,
                      response: ServerResponse): Unit =
    val commandJson = request.content.as(classOf[String])
    logger.info(s"*** Handler command json: $commandJson")

    val command = readFromString[Command](commandJson)
    logger.info(s"*** Handler command: $command")

    val event = dispatcher.dispatch(command)
    logger.info(s"*** Handler event: $event")

    val eventJson = writeToString[Event](event)
    logger.info(s"*** Handler event json: $eventJson")

    response
      .status(200)
      .header("Content-Type", "application/json; charset=UTF-8")
      .send(eventJson)