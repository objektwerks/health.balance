package objektwerks

import com.sun.net.httpserver.HttpServer
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging

import java.net.InetSocketAddress
import java.util.concurrent.Executors

import scala.concurrent.duration.*

object Server extends LazyLogging:
  private val config = ConfigFactory.load("server.conf")
  private val host = config.getString("host")
  private val port = config.getInt("port")
  private val backlog = 0

  private val store = Store(config, Store.cache(minSize = 4, maxSize = 10, expireAfter = 24.hour))
  private val emailer = Emailer(config)
  private val dispatcher = Dispatcher(store, emailer)

  private val http = HttpServer.create(InetSocketAddress(port), backlog)
  private val handler = Handler(dispatcher, store, logger)

  @main def main(): Unit =
    http.setExecutor(Executors.newVirtualThreadPerTaskExecutor())
    http.createContext("/command", handler)

    http.start()
    println(s"*** Press Control-C to shutdown Health Balance Http Server at: $host:$port")
    logger.info(s"*** Health Balance Http Server started at: $host:$port")

    sys.addShutdownHook {
      http.stop(10)
      logger.info(s"*** Health Balance Http Server shutdown at: $host:$port")
    }

    Thread.currentThread().join()