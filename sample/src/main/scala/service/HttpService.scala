package service

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import data.persistence.UserRepository
import routes.UserRoutes

import scala.concurrent.ExecutionContext

trait HttpService {
  implicit lazy val system: ActorSystem = ActorSystem()
  implicit lazy val ex: ExecutionContext = system.dispatcher
  implicit lazy val materializer: ActorMaterializer = ActorMaterializer()

  lazy val userRepo: UserRepository = new UserRepository

  lazy val routes: Route = new UserRoutes(userRepo).routes
}
