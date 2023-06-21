package objektwerks

import com.github.plokhotnyuk.jsoniter_scala.core.*
import com.typesafe.scalalogging.LazyLogging

import java.net.URI
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS
import java.util.concurrent.Executors

import scalafx.application.Platform
import scala.concurrent.{blocking, Await, ExecutionContext, Future}
import scala.concurrent.duration.*
import scala.jdk.FutureConverters.*
import scala.util.Try

import Serializer.given

final class Fetcher(context: Context) extends LazyLogging:
  implicit private val executionContext: ExecutionContext = ExecutionContext.fromExecutor( Executors.newVirtualThreadPerTaskExecutor() )
  private val uri = URI(context.url)
  private val connectError = context.errorServer
  private val client = HttpClient
                         .newBuilder
                         .executor( Executors.newVirtualThreadPerTaskExecutor() )
                         .build

  logger.info(s"*** Fetcher fetching on: ${context.url}")

  private def fromCommandToJson(command: Command): String = writeToString[Command](command)

  private def fromJsonToEvent(json: String): Event = readFromString[Event](json)

  private def toFault(error: Exception): Fault =
    Fault(
      if error.getMessage == null then connectError
      else error.getMessage
    )

  private def buildHttpRequest(json: String): HttpRequest =
    HttpRequest
          .newBuilder
          .uri(uri)
          .timeout(Duration.of(30, SECONDS))
          .version(HttpClient.Version.HTTP_2)
          .headers("Content-Type", "application/json; charset=UTF-8", "Accept", "application/json")
          .POST( HttpRequest.BodyPublishers.ofString(json) )
          .build

  private def sendAsyncHttpRequest(httpRequest: HttpRequest): Future[HttpResponse[String]] =
    client.sendAsync( httpRequest, BodyHandlers.ofString ).asScala

  private def sendBlockingHttpRequest(httpRequest: HttpRequest): HttpResponse[String] =
    val future = Future {
      require(!Platform.isFxApplicationThread, "Http client should not send request in fx thread.")
      blocking {
        client.send( httpRequest, BodyHandlers.ofString )
      }
    }
    Await.result(future, 30.seconds)

  def fetchAsync(command: Command,
                 handler: Event => Unit): Unit =
    logger.info(s"*** fetch async command: $command")
    val commandJson = fromCommandToJson(command)
    val httpRequest = buildHttpRequest(commandJson)

    sendAsyncHttpRequest(httpRequest).map { httpResponse =>
      val eventJson = httpResponse.body 
      val event = fromJsonToEvent(eventJson)
      logger.info(s"*** fetch async event: $event")
      handler(event)
    }.recover { case error: Exception => handler( toFault(error) ) }

  def fetch(command: Command,
            handler: Event => Unit): Unit =
    logger.info(s"*** fetch command: $command")
    val commandJson = fromCommandToJson(command)
    val httpRequest = buildHttpRequest(commandJson)

    val event = Try {
      val httpResponse = sendBlockingHttpRequest(httpRequest)
      val eventJson = httpResponse.body 
      fromJsonToEvent(eventJson)
    }.recover { case error: Exception => toFault(error) }
     .get

    logger.info(s"*** fetch event: $event")
    handler(event)