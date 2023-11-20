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
import scala.concurrent.ExecutionContext
import scala.jdk.FutureConverters.*

import Serializer.given

final class Fetcher(context: Context) extends LazyLogging:
  given ExecutionContext = ExecutionContext.fromExecutor( Executors.newVirtualThreadPerTaskExecutor() )
  private val uri = URI(context.url)
  private val connectError = context.errorServer
  private val client = HttpClient
                         .newBuilder
                         .executor( Executors.newVirtualThreadPerTaskExecutor() )
                         .build

  logger.info(s"*** Fetcher url: ${context.url}")

  private def buildHttpRequest(json: String): HttpRequest =
    HttpRequest
      .newBuilder
      .uri(uri)
      .timeout(Duration.of(30, SECONDS))
      .version(HttpClient.Version.HTTP_2)
      .headers("Content-Type", "application/json; charset=UTF-8", "Accept", "application/json")
      .POST( HttpRequest.BodyPublishers.ofString(json) )
      .build

  def fetch(command: Command,
                 handler: Event => Unit): Unit =
    logger.info(s"*** Fetcher command: $command")
    val commandJson = writeToString[Command](command)
    val httpRequest = buildHttpRequest(commandJson)
    logger.info(s"*** Fetcher http request: $httpRequest")

    client
      .sendAsync(httpRequest, BodyHandlers.ofString)
      .asScala
      .map { httpResponse =>
        val eventJson = httpResponse.body 
        val event = readFromString[Event](eventJson)
        logger.info(s"*** Fetcher event: $event")
        Platform.runLater(handler(event))
      }.recover {
        case error: Exception =>
          val fault = Fault(
            if error.getMessage == null then connectError
            else error.getMessage
          )
          logger.error(s"Fetcher fault: $fault")
          handler(fault)
      }