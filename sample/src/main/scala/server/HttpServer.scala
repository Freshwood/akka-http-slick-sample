package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import config.UserSampleConfig

import scala.concurrent.ExecutionContext

trait HttpServer extends UserSampleConfig {
  implicit def system: ActorSystem

  implicit def materializer: Materializer

  implicit def ec: ExecutionContext

  def routes: Route

  Http().bindAndHandle(routes, httpHost, httpPort)
}
